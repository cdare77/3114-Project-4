
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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

    } // end main()

    private static void parseCommandFile() {
        // TODO
    }

    private static void parseInsert() {
        // TODO
    }

    private static void parseRemove() {
        // TODO
    }

    private static void parsePrint() {
        // TODO
    }

    private static void parseList() {
        // TODO
    }

    private static void parseDelete() {
        // TODO
    }

    private static Handle insertIntoMemory(String name) {
        // mark flag as active since we just inserted it
        byte[] nameArr = name.getBytes();
        short length = (short) nameArr.length;

        // Expand memory whenever current array size is not
        // enough
        if (offset + length + 3 >= memory.length) {
            expandMemory();
        }
        // first byte marks flag
        memory[offset++] = 0x01;
        // store length in little endian order
        memory[offset++] = (byte) (length & 0xff);
        memory[offset++] = (byte) ((length >> 8) & 0xff);
        // copy string into next 'length' bytes
        System.arraycopy(nameArr, 0, memory, offset, length);
        Handle toRet = new Handle(memory, offset, length);
        offset += length;
        return toRet;
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
                // and
                // length
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
