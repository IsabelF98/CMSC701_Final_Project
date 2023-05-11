import argparse


def convert(reference, read):
    with open(reference, "r") as f:
        ref = f.readlines()[1]
    with open(read, "r") as f:
        lines = f.readlines()

    loc = read.find('.')
    filename = read[:loc] + ".seq"

    with open(filename, "w") as f:
        for line in lines:
            if "header" in line:
                continue
            f.write(">" + ref + "\n")
            f.write("<" + line)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Evaluate bloom filter")
    parser.add_argument("ref_filename", type=str,
                        help="path to file of reference sequence")
    parser.add_argument("reads_filename", type=str,
                        help="path to file of read sequences")
    args = parser.parse_args()
    convert(args.ref_filename, args.reads_filename)
