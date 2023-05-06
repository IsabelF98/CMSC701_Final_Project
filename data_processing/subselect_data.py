import argparse


def subselect_data(file, outfile, start, end):
    # Creates a file with the selected data from *file*
    # That is, the reads of length such that start <= length <= end
    # Saves to fasta file named *outfile*
    with open(file, "r") as f:
        lines = f.readlines()

    selected_lines = []
    i = 0
    while i < len(lines):
        header = lines[i]
        pos = header.find("length=")
        length = int(header[pos+7:])
        if start <= length <= end:
            selected_lines.append(header)
            selected_lines.append(lines[i+1])
        i += 2

    with open(outfile, "w") as f:
        f.writelines(selected_lines)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Evaluate bloom filter")
    parser.add_argument("file", type=str,
                        help="path to fasta file")
    parser.add_argument("outfile", type=str,
                        help="path to save output fasta file")
    parser.add_argument("start", type=int,
                        help="Start of range of read lengths to extract")
    parser.add_argument("end", type=int,
                        help="End of range of read lengths to extract")
    args = parser.parse_args()

    subselect_data(file=args.file, outfile=args.outfile,
                   start=args.start, end=args.end)
