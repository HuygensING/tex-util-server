#!/bin/bash
cd $1 && latex -interaction=batchmode -file-line-error -halt-on-error $2.tex && dvisvgm --no-fonts $2.dvi