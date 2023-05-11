def main():
    fname = "generate_data.sh"

    # Lengths of reads:
    n_vals = [100, 150, 200, 250]
    e_vals = [0.05, 0.25, 0.5, 0.75]
    s_vals = [0.95, 0.75, 0.25, 0.05]

    with open(fname, "w") as f:
        f.write("#! /bin/bash\n")
        for n in n_vals:
            for e in e_vals:
                for s in s_vals:
                    ref_fn = f"ref_n{n}_e{int(e*100)}_s{int(s*100)}.fasta"
                    reads_fn = f"reads_n{n}_e{int(e*100)}_s{int(s*100)}.fasta"
                    line = f'''python generate_data.py {ref_fn} {reads_fn} {n} 500 -e {e} -s {s} -d {(1-s)/2} -i {(1-s)/2}\n'''
                    f.write(line)


if __name__ == '__main__':
    main()
