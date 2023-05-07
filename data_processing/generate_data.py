import random
import argparse


def generate_reference(refname, length):
    # Generate a random string of nucleotides of length *length*
    # Saves generated reference to *refname* fasta file

    ref = ''.join((random.choice('ACGT') for _ in range(length)))
    with open(refname, "w") as f:
        f.write(">header 0, generated reference, length=" + str(length) + "\n")
        f.write(ref)
    return ref


def generate_reads(ref, number, e, s=1/3, d=1/3, i=1/3):
    # generate *number* reads of same length as reference
    # with length * e edits at random according to
    # substitution rate *s*, deletion rate *d*, and insertion rate *i*

    N = len(ref)
    reads = []
    for i in range(number):
        # Perform mutations
        n_mut = int(N*e)
        inds = random.sample(range(0, N), n_mut)

        read = ref
        for ind in inds:
            u = random.random()
            char = random.choice('ACGT')
            if u < s:
                read = read[:ind] + char + read[ind+1:]
            elif u < s + d:
                read = read[:ind] + read[ind+1:]
            else:
                read = read[:ind] + char + read[ind:]
        reads.append(read)

    return reads


def save_reads(outfile, reads):
    # Save generated reads to fasta file *outfile*
    header = ">header {}, generated data, length=" + str(len(reads[0]))
    with open(outfile, "w") as f:
        for ind, read in enumerate(reads):
            f.write(header.format(ind) + "\n")
            f.write(read + "\n")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Evaluate bloom filter")
    parser.add_argument("ref_filename", type=str,
                        help="path to save output fasta file")
    parser.add_argument("outfile", type=str,
                        help="path to save output fasta file")
    parser.add_argument("length", type=int,
                        help="Length of read to generate")
    parser.add_argument("n_reads", type=int,
                        help="Number of reads to generate")
    parser.add_argument("-e", "--error_rate", type=float, default=0.01,
                        help="Edit / error rate")
    parser.add_argument("-s", "--sub_rate", type=float, default=1./3,
                        help="Rate of substitutions when an edit occurs")
    parser.add_argument("-i", "--ins_rate", type=float, default=1./3,
                        help="Rate of insertions when an edit occurs")
    parser.add_argument("-d", "--del_rate", type=float, default=1./3,
                        help="Rate of deletions when an edit occurs")
    args = parser.parse_args()

    ref = generate_reference(refname=args.ref_filename,
                             length=args.length)
    reads = generate_reads(ref=ref, number=args.n_reads,
                           e=args.error_rate,
                           s=args.sub_rate,
                           d=args.del_rate,
                           i=args.ins_rate)
    save_reads(outfile=args.outfile, reads=reads)

