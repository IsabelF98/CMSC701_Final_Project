# edit-distance

A project containing a custom algorithm for computing edit distance between two strings, designed to be optimal for short strings (150-300 characters). This program is written in Java, and dependencies are managed via Maven.

## Running the algorithm
Recommended steps to run the program:

1. Check out the code from Github.
2. Run `mvn clean compile assembly:single` in Eclipse or from the command line to compile the project and create the jar.
3. Navigate to `CMSC701_Final_Project/edit-distance/target` folder on your local machine wherever you checked out the code. Verify that the jar has been created in this folder with the name as `edit-distance-1.0-SNAPSHOT-jar-with-dependencies.jar`.
4. Open a command prompt from the `CMSC701_Final_Project/edit-distance/target` folder and run one of the following commands:
* To input the strings as part of the command: run `java -jar edit-distance-1.0-SNAPSHOT-jar-with-dependencies.jar ACGAA ATCGAA -3 -1 1 5` to compute the edit distance on strings `ACGAA` and `ATCGAA` with a gap cost of -3, mismatch cost of -1, match score of 1, and bandwidth of 5 of which to search along the diagonal for solutions. The edit distance score and optimal path information will be printed out on the console.
* To input strings from two FASTA files: run `java -jar edit-distance-1.0-SNAPSHOT-jar-with-dependencies.jar /path_to_reference_file/inputFile1.fna /path_to_reads_file/inputFile2.fna /path_to_output_file/outputFile.txt -3 -1 1 5` to compute the edit distance on string A contained in inputFile1.fna compared to all the strings contained in inputfile2.fna with a gap cost of -3, mismatch cost of -1, match score of 1, and bandwidth of 5 of which to search along the diagonal for solutions. The outputFile.txt will contain contents like the following:
```
ACTTTATC-
A-TTTATCG
1
1765900
```
The first line is string A with `-` to indicate insertions.  The second line is string B with `-` to indicate insertions.  The third line is the edit distance score. The fourth line is the total time, in nanoseconds, to compute the edit distance.  If multiple strings are contained in inputFile2, then there will be multiple of these entries in the output file.

Note that the gap cost, mismatch cost, match score, and bandwidth parameters are all optional. If not provided, the defaults are gap cost of -3, mismatch cost of -1, match score of 1, and bandwidth of the whole edit distance matrix.

Also note that gap cost and mismatch cost must be negative numbers, and match score must be a positive number.

## Components

All the code is contained in the `cmsc701.group.project.edit.distance` package.

### EditDistanceNode
The [EditDistanceNode](src/main/java/cmsc701/group/project/edit/distance/EditDistanceNode.java) is the basic object used to store an edit distance score and the traceback information about the optimal path to an intermediate node (i,j) in the edit distance matrix. The traceback information is stored in the form of two strings (actually StringBuilder which is optimized for concatenation) of forms like `ACAGT` and `AC-GT` where the dash represents an insertion into one of the strings.

### BitVectorNode
The [BitVectorNode](src/main/java/cmsc701/group/project/edit/distance/BitVectorNode.java) is the basic object used in the variant of the algorithm which uses bit vectors to store traceback instead of strings. Similarly to the [EditDistanceNode](src/main/java/cmsc701/group/project/edit/distance/EditDistanceNode.java), it stores an edit distance score and the traceback information about the optimal path to an intermediate node (i,j) in the edit distance matrix. The traceback information is stored in the form of two bit vectors which have 1's to represent insertions into the string and 0's elsewhere.

### CustomAlgorithm
The [CustomAlgorithm](src/main/java/cmsc701/group/project/edit/distance/CustomAlgorithm.java) class contains the algorithm for computing edit distance between two strings. The static `computeEditDistance` method takes in two strings, a gap cost, mismatch cost, match score, and bandwidth and uses these parameters to compute the edit distance. The algorithm stores two columns of the edit distance matrix at one time: the previous column and the current column. These columns each represent one character of string B that we attempt to match with each character of string A.

For each node (i, j) in the edit distance matrix, we compare three paths to node (i, j):
* the diagonal path from node (i-1, j-1)
* the horizontal path from node (i-1, j)
* the vertical path from node (i, j-1)

The horizontal and vertical paths both incur a gap cost to insert/delete a character, while the diagonal path incurs either a mismatch cost or a match score, depending on whether the character in string A at index i matches the character in string B at index j.  Whichever of these paths maximizes the score to node (i, j) is the path that is chosen.  (If multiple paths have the same score, we prefer the diagonal path.)  The traceback information to the current node is updated to include the traceback to the selected previous node.

If the bandwidth parameter is set and is smaller than the number of characters in string B, then instead of computing edit distance values for every node in the edit distance matrix, we stay within a band of the chosen length of the diagonal.

### Main
The [Main](src/main/java/cmsc701/group/project/edit/distance/Main.java) class includes a main method which calls the static `computeEditDistance` method in [CustomAlgorithm](src/main/java/cmsc701/group/project/edit/distance/CustomAlgorithm.java).  When the jar is run from the command line, this main method is the entry point to the program.  See [Running the Algorithm](#running-the-algorithm) for information about how to run the program and what form the input should be in.

In addition to calling `computeEditDistance`, the main method outputs the result to either the console or to the chosen output file.
