
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

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
    // actual data structure holding our data
    private static DynamicByteArray memory;
    // used for constant time searching
    private static HashTable artistTable;
    private static HashTable songTable;
    // trees primarily used for associating pairs of elements
    private static TTTree<KVPair<Handle, Handle>> songTree;
    private static TTTree<KVPair<Handle, Handle>> artistTree;

    /*
     * Used in range searches for one particular artist. Low
     * string lets us use the lexicographically smallest string
     * in the value reference of a KVPair in order to find all
     * instances matching a particular artist name. For example,
     * 
     * tree.rangeSearch(new KVPair<Handle, Handle>(artist,
     * LOW_HANDLE), new KVPair<Handle, Handle>(artist,
     * HIGH_HANDLE))
     * 
     * returns all KVPairs with Key value artist.
     */
    private static final Handle LOW_HANDLE =
            new Handle("".getBytes(), -10, 0);
    private static final Handle HIGH_HANDLE =
            new Handle("".getBytes(), Integer.MAX_VALUE, 0);

    // ------------------- PUBLIC METHODS ----------------------

    /**
     * Compiled with JDK 1.8.0, with JRE's {Java SE 8} using
     * mac0S Sierra version 10.12.6 last compiled on 11/29/17
     * 
     * @param args
     *            - args[0]: initial hash size, args[1]: block
     *            size, args[2]: command file
     */
    public static void main(String[] args) {
        PrintStream old = System.out;
        
        PrintStream ps;
        try {
            ps = new PrintStream(new File("output.txt"));
            System.setOut(ps);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        
        if (args.length < 3) {
            // improper invocation
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

        // Create our 2-3+ tree
        artistTree = new TTTree<KVPair<Handle, Handle>>();
        songTree = new TTTree<KVPair<Handle, Handle>>();

        parseCommandFile();
        
        System.setOut(old);
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
        String[] artistSongString = toInsert.split("<SEP>");

        Handle artist;
        Handle song;

        artist = artistTable.search(artistSongString[0]);
        song = songTable.search(artistSongString[1]);

        if (artist == null) {
            artist = memory.insert(artistSongString[0], true);
            System.out.printf(
                    "|%s| is added to the Artist database.\n",
                    artistSongString[0]);
        }
        else {
            System.out.printf(
                    "|%s| duplicates a record already in the"
                            + " Artist database.\n",
                    artistSongString[0]);
        }
        if (song == null) {
            song = memory.insert(artistSongString[1], false);
            System.out.printf(
                    "|%s| is added to the Song database.\n",
                    artistSongString[1]);
        }
        else {
            System.out.printf(
                    "|%s| duplicates a record already in the"
                            + " Song database.\n",
                    artistSongString[1]);
        }

        KVPair<Handle, Handle> artistSong =
                new KVPair<Handle, Handle>(artist, song);
        KVPair<Handle, Handle> songArtist =
                new KVPair<Handle, Handle>(song, artist);

        if (artistTree.search(artistSong) == null) {
            // insert into tree
            artistTree.insert(artistSong);
            songTree.insert(songArtist);
            System.out.printf(
                    "The KVPair (|%s|,|%s|),(%d,%d) is added to"
                            + " the tree.\n",
                    artist.getStringAt(), song.getStringAt(),
                    artist.getOffset() - 3,
                    song.getOffset() - 3);
            System.out.printf(
                    "The KVPair (|%s|,|%s|),(%d,%d) is added to"
                            + " the tree.\n",
                    song.getStringAt(), artist.getStringAt(),
                    song.getOffset() - 3,
                    artist.getOffset() - 3);
        }
        else {
            // duplicate artist-song pair; do not insert.
            System.out.printf(
                    "The KVPair (|%s|,|%s|),(%d,%d) duplicates a"
                            + " record already in the tree.\n",
                    artist.getStringAt(), song.getStringAt(),
                    artist.getOffset() - 3,
                    song.getOffset() - 3);
            System.out.printf(
                    "The KVPair (|%s|,|%s|),(%d,%d) duplicates a"
                            + " record already in the tree.\n",
                    song.getStringAt(), artist.getStringAt(),
                    song.getOffset() - 3,
                    artist.getOffset() - 3);
        } // end else
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

        // flag which indicates whether our argument was
        // artist or song
        boolean isArtist = argument.contains("artist");
        Handle key;
        Handle value;

        if (isArtist) {
            // search our artist table for the handle
            key = artistTable.search(name);
            if (key == null) {
                // artist not in artistTable
                System.out.printf(
                        "|%s| does not exist in the artist database.\n",
                        name);
                return;
            } // end inner if
        } // end outer if
        else {
            // search our song table for the handle
            key = songTable.search(name);
            if (key == null) {
                // song not in songTable
                System.out.printf(
                        "|%s| does not exist in the song database.\n",
                        name);
                return;
            } // end if
        } // end else

        // Get a list of all pairs in our tree with the same key
        List<KVPair<Handle, Handle>> withThisKey =
                allWithKey(key, isArtist);
        List<KVPair<Handle, Handle>> withSameValue;

        for (KVPair<Handle, Handle> pair : withThisKey) {
            // iterate over list, remove both permutations
            // of this pair
            value = pair.getValue();

            // report that the permutations are removed from the
            // tree
            if (isArtist) {
                artistTree.remove(pair);
                songTree.remove(
                        new KVPair<Handle, Handle>(value, key));
                System.out.printf(
                        "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                        key.getStringAt(), value.getStringAt());
                System.out.printf(
                        "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                        value.getStringAt(), key.getStringAt());
            }
            else {
                songTree.remove(pair);
                artistTree.remove(
                        new KVPair<Handle, Handle>(value, key));
                System.out.printf(
                        "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                        value.getStringAt(), key.getStringAt());
                System.out.printf(
                        "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                        key.getStringAt(), value.getStringAt());
            }

            withSameValue = allWithKey(value, !isArtist);
            if (withSameValue.size() == 0) {
                memory.delete(value, !isArtist);
                if (isArtist) {
                    System.out.printf(
                            "|%s| is deleted from the song database.\n",
                            value.getStringAt());
                }
                else {
                    System.out.printf(
                            "|%s| is deleted from the artist database.\n",
                            value.getStringAt());
                } // end else
            } // end if
        } // end for loop

        memory.delete(key, isArtist);
        if (isArtist) {
            System.out.printf(
                    "|%s| is deleted from the artist database.\n",
                    key.getStringAt());
        }
        else {
            System.out.printf(
                    "|%s| is deleted from the song database.\n",
                    key.getStringAt());
        } // end else

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
            System.out.print(artistTable);
            System.out.printf("total artists: %d\n",
                    artistTable.size());
        }
        else if (argument.contains("song")) {
            // called print song
            System.out.print(songTable);
            System.out.printf("total songs: %d\n",
                    songTable.size());
        }
        else {
            // only thing left is print tree
            System.out.println("Printing artist tree:");
            System.out.print(artistTree);
            System.out.println("Printing song tree:");
            System.out.print(songTree);
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
        Handle toList;
        List<KVPair<Handle, Handle>> list;
        boolean isArtist = argument.contains("artist");

        if (isArtist) {
            // current argument specifies artist
            toList = artistTable.search(name);
            if (toList == null) {
                // artist not in hash table
                System.out.printf(
                        "|%s| does not exist in the artist database.\n",
                        name);
                return;
            } // end inner if
        } // end outer if
        else {
            // current argument specifies song
            toList = songTable.search(name);
            if (toList == null) {
                // song not in hash table
                System.out.printf(
                        "|%s| does not exist in the song database.\n",
                        name);
                return;
            }
        } // end else

        list = allWithKey(toList, isArtist);

        for (KVPair<Handle, Handle> pair : list) {
            System.out.printf("|%s|\n",
                    pair.getValue().getStringAt());
        } // end for-loop
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

        // Check to make sure song-artist pair exist. If not,
        // report and exit
        if (artistHandle == null) {
            // artist not in artist hash table
            System.out.printf(
                    "|%s| does not exist in the artist database.\n",
                    artistSong[0]);
            // replace null value with a value that we know won't
            // be in table
            artistHandle = LOW_HANDLE;
            songHandle = LOW_HANDLE;
        }
        else if (songHandle == null) {
            // song not in song hash table
            System.out.printf(
                    "|%s| does not exist in the song database.\n",
                    artistSong[1]);
            // replace null value with a value that we know won't
            // be in table
            songHandle = LOW_HANDLE;
            artistHandle = LOW_HANDLE;
        }

        // remove this specific combination from both
        // trees
        KVPair<Handle, Handle> removedArtist =
                artistTree.remove(
                        new KVPair<Handle, Handle>(artistHandle,
                                songHandle));
        KVPair<Handle, Handle> removedSong =
                songTree.remove(
                        new KVPair<Handle, Handle>(songHandle,
                                artistHandle));

        if (removedArtist != null && removedSong != null) {
            // song exists in the database

            System.out.printf(
                    "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                    artistHandle.getStringAt(),
                    songHandle.getStringAt());
            System.out.printf(
                    "The KVPair (|%s|,|%s|) is deleted from the tree.\n",
                    songHandle.getStringAt(),
                    artistHandle.getStringAt());

            // likely does not have any worse efficiency than
            // O(log n) unless this artist or song takes up a
            // significant portion of the table
            int thisSongLeft =
                    allWithKey(songHandle, false).size();
            int thisArtistLeft =
                    allWithKey(artistHandle, true).size();

            if (thisArtistLeft == 0) {
                // there are no more instances of this artist
                // having any other songs, so we remove all
                // references to it
                memory.delete(artistHandle, true);
                System.out.printf(
                        "|%s| is deleted from the artist database.\n",
                        artistHandle.getStringAt());
            }
            if (thisSongLeft == 0) {
                // there are no more instances of this song
                // attached to any other artist, so we remove all
                // references to it
                memory.delete(songHandle, false);
                System.out.printf(
                        "|%s| is deleted from the song database.\n",
                        songHandle.getStringAt());
            } // end inner if
        } // end if (song and artist existed in database)
        else {
            System.out.printf(
                    "The KVPair (|%s|,|%s|) was not found in the database.\n",
                    artistSong[0],
                    artistSong[1]);
            System.out.printf(
                    "The KVPair (|%s|,|%s|) was not found in the database.\n",
                    artistSong[1],
                    artistSong[0]);

        } // end else
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
     * @return list of all song-artist pairs matching artist/song
     *         in handle
     */
    private static List<KVPair<Handle, Handle>> allWithKey(
            Handle handle, boolean isArtist) {
        if (isArtist) {
            return artistTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_HANDLE),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_HANDLE));
        }
        else {
            return songTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_HANDLE),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_HANDLE));
        }
    } // end allWithKey
} // end SongSearch
