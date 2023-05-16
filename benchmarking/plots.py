#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
This file creates the runtime plots for the different algorithms.
"""

import pandas as pd
import matplotlib.pyplot as plt


# Lengths of reads:
n_vals = [100, 150, 200, 250]
# Error rate
e_vals = [0.05, 0.25, 0.5, 0.75]
# Substitution vs. Deletion ratio
s_vals = [0.95, 0.75, 0.25, 0.05]

# load data
astar_data_df   = pd.read_csv('synth_astar_time.csv')
edlib_data_df   = pd.read_csv('synth_edlib_time.csv')
wfa_data_df     = pd.read_csv('synth_wfa_time.csv')
minimap_data_df = pd.read_csv('synth_minimap_time.csv')

columns = ["e="+str(e)+",s="+str(s) for e in e_vals for s in s_vals]
custom_bitvec = pd.read_csv('outputFile_bitVector.csv', names=columns)
custom_bitvec_ban = pd.read_csv('outputFile_bitVectorBanded.csv', names=columns)
custom_no_trace = pd.read_csv('outputFile_noTraceback.csv', names=columns)
custom_no_trace_ban = pd.read_csv('outputFile_noTracebackBanded.csv', names=columns)
custom_w_trace = pd.read_csv('outputFile_withTraceback.csv', names=columns)
custom_w_trace_ban = pd.read_csv('outputFile_withTracebackBanded.csv', names=columns)


# custom alg plots
fig = plt.figure(figsize=(22,20))
plt.rcParams.update({'font.size': 10})
i = 1
for e in e_vals:
    for s in s_vals:
        plt.subplot(4, 4, i)
        plt.plot(n_vals, custom_bitvec["e="+str(e)+",s="+str(s)], label="Bit-Vevtor", marker='o')
        plt.plot(n_vals, custom_bitvec_ban["e="+str(e)+",s="+str(s)], label="Bit-Vevtor Banded", marker='o')
        plt.plot(n_vals, custom_no_trace["e="+str(e)+",s="+str(s)], label="no Traceback", marker='o')
        plt.plot(n_vals, custom_no_trace_ban["e="+str(e)+",s="+str(s)], label="no Traceback Banded", marker='o')
        plt.plot(n_vals, custom_w_trace["e="+str(e)+",s="+str(s)], label="w/ Traceback", marker='o')
        plt.plot(n_vals, custom_w_trace_ban["e="+str(e)+",s="+str(s)], label="w/ Traceback Banded", marker='o')
        plt.title("error rate="+str(e)+", sub/del ratio="+str(s))
        plt.xlabel("n")
        plt.ylabel("time (sec)")
        plt.legend()
        i += 1        
fig.suptitle('Custom Alg. Throughput Runtime', size=16)  

# all alg plots
fig = plt.figure(figsize=(22,20))
plt.rcParams.update({'font.size': 10})
i = 1
for e in e_vals:
    for s in s_vals:
        plt.subplot(4, 4, i)
        plt.plot(n_vals, edlib_data_df["e="+str(e)+",s="+str(s)]/500, label="Edlib", marker='o')
        plt.plot(n_vals, wfa_data_df["e="+str(e)+",s="+str(s)]/500, label="WFA", marker='o')
        plt.plot(n_vals, minimap_data_df["e="+str(e)+",s="+str(s)]/500, label="Minimap2", marker='o')
        plt.plot(n_vals, custom_no_trace_ban["e="+str(e)+",s="+str(s)], label="Custom", marker='o')
        plt.plot(n_vals, astar_data_df["e="+str(e)+",s="+str(s)]/500, label="Astar", marker='o')
        plt.title("error rate="+str(e)+", sub/del ratio="+str(s))
        plt.xlabel("n")
        plt.ylabel("time (sec)")
        plt.legend()
        i += 1
fig.suptitle('Throughput Runtime', size=16)

# single plot
e = 0.75
s = 0.75
fig = plt.figure(figsize=(12,10))
plt.rcParams.update({'font.size': 18})
plt.plot(n_vals, edlib_data_df["e="+str(e)+",s="+str(s)]/500, label="Edlib", marker='o')
plt.plot(n_vals, wfa_data_df["e="+str(e)+",s="+str(s)]/500, label="WFA", marker='o')
plt.plot(n_vals, minimap_data_df["e="+str(e)+",s="+str(s)]/500, label="Minimap2", marker='o')
plt.plot(n_vals, custom_no_trace_ban["e="+str(e)+",s="+str(s)], label="Custom", marker='o')
plt.title("error rate="+str(e)+", sub/del ratio="+str(s))
plt.xlabel("n")
plt.ylabel("time (sec)")
plt.legend()
