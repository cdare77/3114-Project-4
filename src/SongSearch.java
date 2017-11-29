
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.management.InstanceNotFoundException;

/**
 * Main driver for Project 4 Song Database. Parses an input file
 * as instructions for our database, supporting:
 * 
 * insert {artist-name}<SEP>{song-name} remove {artist|song}
 * {name} print {artist|song} list {artist|song} {name} delete
 * {artist-name}<SEP>{song-name} print tree
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

    private static byte[] memory;
    private static File commandFile;
    private static int initialHashSize;
    private static int blockSize;
    private static HashTable artistTable;
    private static HashTable songTable;
    private static TTTree<KVPair<Handle, Handle>> artistTree;
    private static TTTree<KVPair<Handle, Handle>> songTree;
    private static int offset;

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
     * mac0S Sierra version 10.12.6 last compiled on 11/08/17
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

        memory = new byte[blockSize];
        offset = 0;

        // Create our hash tables
        artistTable = new HashTable(initialHashSize);
        songTable = new HashTable(initialHashSize);

        parseCommandFile();
    } // end main()

    private static void parseCommandFile() {
        try {
            Scanner scan = new Scanner(commandFile);
            String command;
            while (scan.hasNext()) {
                command = scan.next();

                if (command.contains("insert")) {
                    parseInsert(scan);
                }
                else if (command.contains("print")) {
                    parsePrint(scan);
                }
                // TODO rest of commands
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        String[] artistSong =
                scan.next().split("<SEP>");
        // create references in memory to
        Handle artist = artistTable.search(artistSong[0]);
        Handle song = songTable.search(artistSong[1]);

        // if either string is not contained in memory,
        // we create a handle and insert it into memory
        if (artist == null) {
            // artist is not in memory

            // first insert bytes into memory and return
            // handle pointing to position
            artist = insertIntoMemory(artistSong[0]);
            // next add handle to artist hash table
            artistTable.insert(artist);
        }
        if (song == null) {
            // song not in memory

            // first insert bytes into memory and return
            // handle pointing to position
            song = insertIntoMemory(artistSong[1]);
            // next add handle to song hash table
            songTable.insert(song);
        }

        // regardless of whether either artist or song were in
        // memory, we want to add the pair to both trees so that
        // their references are joined
        artistTree.insert(new KVPair<Handle, Handle>(
                artist, song));
        songTree.insert(new KVPair<Handle, Handle>(
                song, artist));
    } // end parseInsert

    private static void parseRemove(Scanner scan)
            throws InstanceNotFoundException {
        
        String argument = scan.next();
        String name = scan.next();

        if (argument.contains("artist")) {
            Handle artistHandle = artistTable.search(name);

            if (artistHandle == null) {
                // TODO artist not in table
                return;
            }
            List<KVPair<Handle, Handle>> byThisArtist =
                    allWithKey(artistHandle, true);
            List<KVPair<Handle, Handle>> hasThisSong;
            Handle songHandle;

            for (KVPair<Handle, Handle> pair : byThisArtist) {
                songHandle = pair.getValue();

                artistTree.remove(pair);
                songTree.remove(new KVPair<Handle, Handle>(
                        songHandle, artistHandle));
                hasThisSong = allWithKey(songHandle, false);

                if (hasThisSong.size() == 0) {
                    cleanUpReferences(songHandle, false);
                }
            }

            cleanUpReferences(artistHandle, true);
        }
        else {
            Handle songHandle = songTable.search(name);

            if (songHandle == null) {
                // TODO song not in table
                return;
            }
            List<KVPair<Handle, Handle>> hasThisSong =
                    allWithKey(songHandle, false);
            List<KVPair<Handle, Handle>> byThisArtist;
            Handle artistHandle;

            for (KVPair<Handle, Handle> pair : hasThisSong) {
                artistHandle = pair.getValue();

                songTree.remove(pair);
                artistTree.remove(new KVPair<Handle, Handle>(
                        artistHandle, songHandle));

                byThisArtist = allWithKey(artistHandle, true);

                if (byThisArtist.size() == 0) {
                    cleanUpReferences(artistHandle, true);
                }
            }

            cleanUpReferences(songHandle, false);
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

    private static void parseList() {
        // TODO
    }

    private static void parseDelete(Scanner scan) {
        String[] artistSong =
                scan.next().split("<SEP>");
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
                cleanUpReferences(songHandle, false);
            }
            if (thisArtistLeft == 0) {
                cleanUpReferences(artistHandle, true);
            }

        } // end try
        catch (InstanceNotFoundException e) {
            // TODO either invalid artist or invalid song
            return;
        } // end catch

    } // end parseDelete

    private static Handle insertIntoMemory(String name) {
        // find number of bytes to insert into memory
        byte[] nameArr = name.getBytes();
        short length = (short) nameArr.length;
        // Expand memory whenever current array size is not
        // enough
        if (offset + length + 3 >= memory.length) {
            expandMemory();
        }
        // mark flag as active since we just inserted it
        memory[offset++] = 0x01;
        // store length in little endian order
        memory[offset++] = (byte) (length & 0xff);
        memory[offset++] = (byte) ((length >> 8) & 0xff);
        // copy string into next 'length' bytes
        System.arraycopy(nameArr, 0, memory, offset,
                length);
        // update pointer to offset
        offset += length;
        // return new handle pointing to record just inserted
        return new Handle(memory, offset - length, length);
    }

    private static void deleteFromMemory(Handle handle) {
        // make corresponding marks to memory (null
        // out the flag)
        int offset = handle.getOffset();
        memory[offset - 3] = 0x00;
    }

    private static void cleanUpReferences(Handle handleName,
            boolean isArtist) {
        if (isArtist) {
            artistTable.delete(handleName);
        }
        else {
            songTable.delete(handleName);
        }
        deleteFromMemory(handleName);
    }

    private static List<KVPair<Handle, Handle>> allWithKey(
            Handle handle, boolean keyIsArtist) {
        if (keyIsArtist) {
            return artistTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_STRING),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_STRING));
        }
        else {
            return songTree.rangeSearch(
                    new KVPair<Handle, Handle>(handle,
                            LOW_STRING),
                    new KVPair<Handle, Handle>(handle,
                            HIGH_STRING));
        }
    }

    private static void expandMemory() {
        byte[] newMemory = new byte[memory.length + blockSize];
        short length;
        int newOffset = 0;

        // iterate over our current memory
        for (int i = 0; i < memory.length; i += length + 3) {

            // get length from second and third byte after offset
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(memory[i + 1]);
            bb.put(memory[i + 2]);
            length = bb.getShort(0);

            if (memory[i] != 0x00) {
                // Flag is nonzero which means that we should
                // copy data over to new array
                String searchString = new String(
                        Arrays.copyOfRange(memory, i + 3,
                                i + 3 + length));

                // Move data over to new array, including flag
                // and length
                System.arraycopy(memory, i, newMemory, newOffset,
                        length + 3);

                // modify the handle pointing to this data
                // so that it points to new location
                Handle artist = artistTable.search(searchString);
                Handle song = songTable.search(searchString);

                if (song != null && song.getOffset() == i + 3) {
                    // data pointed to is a song
                    song.setOffset(newOffset + 3);
                    song.setMemory(newMemory);
                }
                else {
                    // data pointed to is an artist
                    artist.setOffset(newOffset + 3);
                    artist.setMemory(newMemory);
                }
                // update offset pointer
                newOffset += length + 3;
            } // end if (valid element to copy)

        } // end for

        memory = newMemory;
    } // end expandMemory

} // end SongSearch
