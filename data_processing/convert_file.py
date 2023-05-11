import argparse


def convert(filename):
    with open(filename, "r") as f:
        lines = f.readlines()

    loc = filename.find('.')
    filename = filename[:loc] + ".seq"

    counter = 0
    with open(filename, "w") as f:
        for line in lines:
            if "header" in line:
                continue
            if counter % 2 == 0:
                f.write(">" + line)
            else:
                f.write("<" + line)
            counter += 1


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Evaluate bloom filter")
    parser.add_argument("filename", type=str,
                        help="path to file to convert")
    args = parser.parse_args()
    convert(filename=args.filename)
