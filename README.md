# Assignment1
# Architecture Notes

## This project implements four classic divide-and-conquer algorithms with careful control of recursion depth and memory allocations:

1. MergeSort
  Uses a reusable buffer to avoid repeated allocations during merging. For small subarrays (n ≤ 16), it switches to insertion sort to reduce constant factors. Recursion depth is bounded by O(log n).

2. QuickSort
  Uses a randomized pivot to prevent worst-case behavior. Always recurses on the smaller partition and iterates on the larger one, ensuring recursion depth stays bounded at O(log n) on expectation. For very small ranges, it switches to insertion sort.

3. Deterministic Select (Median-of-Medians)
Implements the MoM5 strategy: group into chunks of 5, sort each chunk, recursively find the median of medians, and partition in place. Only recurses into the side that contains the k-th element, preferring the smaller side to bound recursion depth. Runs in O(n) worst case.

4. Closest Pair of Points (2D)
Implements the classic divide-and-conquer approach:

Sort points by x-coordinate.

Recursively compute the closest pair on the left and right halves.

Merge step: scan a vertical strip around the division line, using y-sorted order, checking only 7–8 neighbors.
Recursion depth is O(log n).

Metrics (time, recursion depth, comparisons) are collected using a lightweight Metrics utility. The CLI (Main.java) runs experiments and emits results to CSV.

# Recurrence Analysis
### MergeSort

Recurrence:

<center>𝑇(𝑛) = 2𝑇(𝑛/2) + Θ(𝑛)T(n)

By the Master Theorem, Case 2:

<center>𝑇(𝑛) = Θ(𝑛log𝑛)

## QuickSort (randomized pivot, smaller-first recursion)

Expected recurrence:

<center>𝑇(𝑛) = 𝑇(smaller part) + 𝑇(larger part) + Θ(𝑛)

Expected balanced splits → average case:

<center>𝑇(𝑛) = Θ(𝑛log⁡𝑛)

Depth bounded by O(log n) (tail recursion optimization ensures stack ≲ 2⌊log₂ n⌋).

## Deterministic Select (Median-of-Medians)

Recurrence:

<center>𝑇(𝑛) = 𝑇(𝑛/5) + 𝑇(7𝑛/10) + Θ(𝑛)

By Akra–Bazzi method:

<center>𝑇(𝑛) = Θ(𝑛)

## Closest Pair of Points (2D)

Recurrence:

<center>𝑇(𝑛) = 2𝑇(𝑛/2) + Θ(𝑛)

The sorting step adds Θ(n log n) at the start, so overall:

<center>𝑇(𝑛)=Θ(𝑛log⁡𝑛)

## Experimental Plots (expected)

Time vs n:

1. MergeSort and QuickSort: upward curve ~n log n.

2. Select: nearly linear.

3. Closest Pair: ~n log n.

Depth vs n:

1. MergeSort and Closest Pair: grows logarithmically.

2. QuickSort: log-like, bounded by ~2 log₂ n.

3. Select: grows slowly, proportional to log n.

## Constant factors:

Insertion sort cutoff improves runtime for small subarrays.

Buffer reuse in MergeSort reduces GC pressure.

Cache effects matter in Closest Pair’s strip checking.

## Summary

Recursion depth control:

Verified QuickSort depth stayed bounded at ~2 log₂ n.

MergeSort and Closest Pair also showed logarithmic depth.

Select recursion depth remained small due to smaller-side recursion.

Conclusion:
The implemented algorithms align closely with theoretical expectations. Constant-factor optimizations (cutoffs, buffer reuse, randomized pivots) significantly improved performance in practice.
