package es.istr.unican.jmastanalysis.utils;

import java.io.*;
import java.util.*;

/**
 * Created by JuanCTR on 03/09/2015.
 */
public class Utils {

    public static byte[] doublesToBytes(double[] values) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < values.length; ++i) {
            try {
                dos.writeDouble(values[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    public static double[] bytesToDoubles(byte[] values) {
        ByteArrayInputStream bais = new ByteArrayInputStream(values);
        DataInputStream dis = new DataInputStream(bais);

        double[] d = new double[values.length / 8]; // 1 double = 8 bytes
        for (int i = 0; i < d.length; i++) {
            try {
                d[i] = dis.readDouble();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    /*
        Creates an array of numbers from first to last: [first, first+1, ..., last]
     */
    public static Integer[] range(int first, int last) {
        if (last < first) {
            return new Integer[0];
        }
        Integer[] numbers = new Integer[last - first + 1];
        for (int i = first; i <= last; i++) {
            numbers[i - first] = i;
        }
        return numbers;
    }


    /*
        shuffles [1, 2, .., inputRangeLength] to an array of length outputLength (output can be longer or shorter)
     */
    public static List<Integer> shuffleToList(int inputRangeLength, int outputLength, Random r) {

        ArrayList<Integer> input = new ArrayList<Integer>(Arrays.asList(range(0, inputRangeLength - 1)));   // Create input range of values [1, 2, 3, ..., inputRangeLength

        // Shuffle input
        Collections.shuffle(input, r);

        // Add elements to list until it reaches outputLength length
        for (int i = 1; i <= outputLength - inputRangeLength; i++) {
            input.add(r.nextInt(inputRangeLength));
        }

        // Slice list to requested length, and return as array of Integers
        return input.subList(0, outputLength);
    }

    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max-min+1)+min;
    }


    public static void main(String[] args) {

        System.out.println(shuffleToList(5, 20, new Random()).toString());

    }
}
