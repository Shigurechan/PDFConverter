#!/bin/bash

clear
javac -verbose -cp lib/pdfbox.jar:lib/commons.jar: src/*.java -d bin    # コンパイル
if [ $? = "0" ] ; then
    jar cvfm PDFConverter.jar manifest.mf -C bin ./                         # .jarファイル生成
    java -jar PDFConverter.jar                                              # .jar実行
    else
        echo -e
        echo -e

        echo "          Compile Error"

        echo -e
        echo -e   
    fi
