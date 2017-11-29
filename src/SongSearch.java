
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
//
// -- Chris Dare

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import javax.management.InstanceNotFoundException;

/**
 * Main driver for Project 4 Song Database. Parses an input file
 * as instructions for our database, supporting:
 * 
 * insert {artist-name}<SEP>{song-name}; remove {artist|song}
 * {name}; print {artist|song}; list {artist|song} {name}; delete
 * {artist-name}<SEP>{song-name}; print tree
 * 
 * Our database is built off two 2-3+ Trees and two hash tables,
 * one of each for keeping track of artists and one of each for
 * keeping track of songs. Each 2-3+ Tree is primarily used for
 * range query purposes, whereas the hash table is intended
 * primarily for regular name-based queries.
 *
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/26/2017
 */
public class SongSearch {

    // ------------------- PRIVATE VARIABLES -------------------

    private static File commandFile;
    private static int initialHashSize;
    private static int blockSize;
    private static DynamicByteArray memory;
    private static HashTable artistTable;
    private static HashTable songTable;
    private static TTTree<KVPair<Handle, Handle>> artistTree;
    private static TTTree<KVPair<Handle, Handle>> songTree;

    /*
     * Used in range searches for one particular artist. Low
     * string lets us use the lexicographically smallest string
     * in the value reference of a KVPair in order to find all
     * instances matching a particular artist name. For example,
     * 
     * tree.rangeSearch(new KVPair<Handle, Handle>(artist,
     * LOW_STRING), new KVPair<Handle, Handle>(artist,
     * HIGH_STRING))
     * 
     * returns all KVPairs with Key value artist.
     */
    private static final Handle LOW_STRING =
            new Handle("".getBytes(), 0, 0);
    private static final Handle HIGH_STRING =
            new Handle("~~~~~~~~~~~~~~~~~".getBytes(), 0, 17);

    // ------------------- PUBLIC METHODS ----------------------

    /**
     * Compiled with JDK 1.8.0, with JRE's {Java SE 8} using
     * mac0S Sierra version 10.12.6 last compiled on 11/29/17
     * 
     * @param args
     *            - args[0] -- initial hash size, args[1] --
     *            block size, args[2] -- command file
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(
                    "Improper invocation\n\n\t\tjava"
                            + " SongSearch {initial-hash-size}"
                            + " {block-size} {command-file}");
            return;
        }
        initialHashSize = Integer.parseInt(args[0]);
        blockSize = Integer.parseInt(args[1]);

        commandFile = new File(args[2]);
        if (!commandFile.exists()) {
            System.out.printf("Command file %s does not exist\n",
                    args[2]);
            return;
        }

        // Create our hash tables
        artistTable = new HashTable(initialHashSize);
        songTable = new HashTable(initialHashSize);

        // link our memory to both hash tables
        memory = new DynamicByteArray(blockSize, artistTable,
                songTable);

        // Create our 2-3+ trees
        artistTree = new TTTree<KVPair<Handle, Handle>>();
        songTree = new TTTree<KVPair<Handle, Handle>>();

        parseCommandFile();
    } // end main()

    // ------------------- PRIVATE METHODS ----------------------

    /**
     * Private helper method which ties together the
     * functionality of all our other parse private helper
     * methods in order to fully traverse the command file.
     */
    private static void parseCommandFile() {
        try {
            Scanner scan = new Scanner(commandFile);
            String command; // first word of each line

            while (scan.hasNext()) {
                // while there is still data in the table
                command = scan.next();

                if (command.contains("insert")) {
                    parseInsert(scan);
                }
                else if (command.contains("print")) {
                    parsePrint(scan);
                }
                else if (command.contains("list")) {
                    parseList(scan);
                }
                else if (command.contains("remove")) {
                    parseRemove(scan);
                }
                else if (command.contains("delete")) {
                    parseDelete(scan);
                }
                else {
                    System.out.printf("Invalid command: %s\n",
                            command);
                    // TODO invalid command
                } // end else
            } // end while
        } // end try
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } // end catch
    } // end parseCommandFile

    /**
     * Inserts an artist and song from our command file into main
     * memory, thus creating handles pointing to each, and
     * inserting those handles into their respective data
     * structures.
     * 
     * format: insert {artist-name}<SEP>{song-name}
     * 
     * @param scan
     *            -- scanner currently used to read command file
     */
    private static void parseInsert(Scanner scan) {
        // Parse the next String as the concatenation
        // of our artist and song separated by delimiter <SEP>
        String toInsert = scan.nextLine().substring(1);
        String[] artistSong = toInsert.split("<SEP>");
        
        if (artistSong[1].equals("Breath")) {
            System.out.print("");
        }

        // Create references in memory to both artist and
        // song. The functionality of DynamicByteArray prevents
        // us from inserting duplicates, but instead returns
        // a reference to the Handle
        Handle artist = memory.insert(artistSong[0], true);
        Handle song = memory.insert(artistSong[1], false);

        artistTree.insert(new KVPair<Handle, Handle>(
                artist, song));
        songTree.insert(new KVPair<Handle, Handle>(
                song, artist));
    } // end parseInsert

    /**
     * Removes an artist or song from memory, making corrections
     * to all our data structures. The first step is getting a
     * list of all song-artist pairs connected to this
     * song/artist. We then proceed to remove every pair from
     * both trees, while removing from memory and hash tables if
     * no other song-artist pairs point to this song/artist.
     * 
     * format: remove {artist|song} {name}
     * 
     * @param scan
     *            -- scanner currently used to read command file
     */
    private static void parseRemove(Scanner scan) {
        // get the next two arguments in our current
        // command
        String argument = scan.next();
        String name = scan.nextLine().substring(1);

        try {
            if (argument.contains("artist")) {
                // artist we are trying to find all song-artist
                // pairs of
                Handle artistHandle = artistTable.search(name);

                if (artistHandle == null) {
                    // TODO artist not in table
                    return;
                }

                // get a list of all song-artist pairs with
                // artist name "name"
                List<KVPair<Handle, Handle>> byThisArtist =
                        allWithKey(artistHandle, true);
                // used to check how many artists each song is
                // linked to
                List<KVPair<Handle, Handle>> hasThisSong;
                // pointer to current song
                Handle songHandle;

                // iterate over list of song-artist pairs by
                // artist "name"
                for (KVPair<Handle,
                        Handle> pair : byThisArtist) {
                    // current song
                    songHandle = pair.getValue();

                    // remove the pair from both trees
                    artistTree.remove(pair);
                    songTree.remove(new KVPair<Handle, Handle>(
                            songHandle, artistHandle));

                    // Check if there are no other artists
                    // connected to this song
                    hasThisSong = allWithKey(songHandle, false);

                    if (hasThisSong.size() == 0) {
                        // there are no other artists connected
                        // to this song so we can simply wipe it
                        // from memory and its hash table
                        memory.delete(songHandle, false);
                    } // end if
                } // end for

                memory.delete(artistHandle, true);
            } // end if (argument artist)
            else {
                // song we are trying to find all song-artist
                // pairs of
                Handle songHandle = songTable.search(name);

                if (songHandle == null) {
                    // TODO song not in table
                    return;
                }
                // get a list of all song-artist with songname
                // "name"
                List<KVPair<Handle, Handle>> hasThisSong =
                        allWithKey(songHandle, false);
                // used to check how many songs each artist is
                // linked to
                List<KVPair<Handle, Handle>> byThisArtist;
                // pointer to curret artist
                Handle artistHandle;

                // iterate over list of song-artist pairs with
                // songname "name"
                for (KVPair<Handle, Handle> pair : hasThisSong) {
                    // current artist
                    artistHandle = pair.getValue();
                    // remove the pair from both trees
                    songTree.remove(pair);
                    artistTree.remove(new KVPair<Handle, Handle>(
                            artistHandle, songHandle));

                    // check if we can remove artist from memory
                    // and hash tables. Since we just removed a
                    // song-artist pair, we need to see if this
                    // artist pops up again
                    byThisArtist =
                            allWithKey(artistHandle, true);

                    if (byThisArtist.size() == 0) {
                        // if there are no more songs tied to
                        // this artist, we simply remove the
                        // artist from memory and the hash table
                        memory.delete(artistHandle, true);
                    } // end if
                } // end for

                // we just removed all instances of this song, so
                // we should remove it from memory and the hash
                // table
                memory.delete(songHandle, false);
            } // end else (argument song)
        }
        catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
    } // end parseRemove

    /**
     * Prints the contents of one or two of our data structures
     * depending on the next argument provided. If we wish to
     * print all the artists or songs, we simply move
     * sequentially through the respective hashTable, printing
     * off all elements and their positions. However, if we wish
     * to print our tree, we must print both the tree
     * corresponding to artist and the tree corresponding to
     * song.
     * 
     * format: print {artist|song|tree}
     * 
     * @param scan
     *            -- scanner currently used to read command file
     */
    private static void parsePrint(Scanner scan) {
        // flag following print. Should be either artist, song,
        // or tree
        String argument = scan.next();

        if (argument.contains("artist")) {
            // called print artist
            System.out.println(artistTable);
        }
        else if (argument.contains("song")) {
            // called print song
            System.out.println(songTable);
        }
        else {
            // only thing left is print tree
            System.out.println(artistTree);
            System.out.println(songTree);
        }
    } // end parsePrint

    /**
     * Calls upon the functionality of our range search to find
     * all songs by a particular artist or find all artists
     * associated with the name of a certain song. Our private
     * helper method allWithKey already returns a list, so we
     * simply print the list returned.
     * 
     * format: list {artist|song} {name}
     * 
     * @param scan
     *            -- scanner currently used to read command file
     */
    private static void parseList(Scanner scan) {
        String argument = scan.next();
        String name = scan.nextLine().substring(1);

        List<KVPair<Handle, Handle>> list;

        if (argument.contains("artist")) {
            // current argument specifies artist
            Handle artistHandle = artistTable.search(name);
            if (artistHandle == null) {
                // TODO artist not in table
                return;
            }
            // get all songs by this artist
            list = allWithKey(artistHandle, true);
            // print list of songs
            System.out.println(list);
        }
        else {
            // current argument specifies song
            Handle songHandle = songTable.search(name);
            if (songHandle == null) {
                // TODO song not in table
                return;
            }
            // get all artists having a song with this name
            list = allWithKey(songHandle, false);
            // print list of artists
            System.out.println(list);
        } // end else
    } // end parseList

    /**
     * Deletes a single artist-song pair from memory. We must
     * first check if the song and artist are both valid. If
     * there are no issues, we proceed by removing both the
     * song-artist and artist-song pairs from both trees.
     * However, we will only remove the arist and song references
     * if there are no other pairs with the same artist or the
     * same song. For example, if two artists have a song of the
     * same (a1, s1) (a2, s1) and we call
     * 
     * delete a1<SEP>s1
     * 
     * we will not remove s1 from memory or the song hash table
     * since that handle is still being used by (a2, s1).
     * 
     * Format: delete {artist-name}<SEP>{song-name}
     * 
     * @param scan
     *            -- scanner currently used to read command file
     */
    private static void parseDelete(Scanner scan) {
        // Parse the next String as the concatenation
        // of our artist and song separated by delimiter <SEP>
        String toInsert = scan.nextLine().substring(1);
        String[] artistSong = toInsert.split("<SEP>");

        Handle artistHandle = artistTable.search(artistSong[0]);
        Handle songHandle = songTable.search(artistSong[1]);

        if (artistHandle == null || songHandle == null) {
            // TODO either invalid artist or invalid song
            return;
        }

        // remove this specific combination from both
        // trees
        try {
            artistTree.remove(new KVPair<Handle, Handle>(
                    artistHandle, songHandle));
            songTree.remove(
                    new KVPair<Handle, Handle>(songHandle,
                            artistHandle));

            // likely does not have any worse efficiency than
            // O(log n) unless this artist or song takes up a
            // significant portion of the table
            int thisSongLeft =
                    allWithKey(songHandle, false).size();
            int thisArtistLeft =
                    allWithKey(artistHandle, true).size();

            if (thisSongLeft == 0) {
                // there are no more instances of this song
                // attached to any other artist, so we remove all
                // references to it
                memory.delete(songHandle, false);
            }
            if (thisArtistLeft == 0) {
                // there are no more instances of this artist
                // having any other songs, so we remove all
                // references to it
                memory.delete(artistHandle, true);
            }

        } // end try
        catch (InstanceNotFoundException e) {
            // TODO either invalid artist or invalid song
            return;
        } // end catch

    } // end parseDelete

    /**
     * Calls on the functionality of range search to look for all
     * song-artist pairs with the specified song / artist,
     * returning found pairs in the format of a list
     * 
     * @param handle
     *            -- artist/song pointer. If an artist handle,
     *            this will return all songs by artist. If a song
     *            handle, this will return all artists with this
     *            song
     * @param keyIsArtist
     *            -- flag which tells us whether handle points to
     *            an artist or song
     * @return list of all song-artist pairs matching artist/song
     *         in handle
     */
    private static List<KVPair<Handle, Handle>> allWithKey(
            Handle handle, boolean keyIsArtist) {

        if (keyIsArtist) {
            // handle points to an artist, thus we call
            // rangeSearch
            // on our artist tree to get all songs by this artist
            return artistTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_STRING),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_STRING));
        }
        else {
            // handle points to a song, thus we call rangeSearch
            // on our song tree to get all artists with a
            // songname pointed to by handle
            return songTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_STRING),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_STRING));
        } // end else
    } // end allWithKey
} // end SongSearch
