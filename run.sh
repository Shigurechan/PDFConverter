#!/bin/bash

result="0" # 結果
cmdCompile="javac -verbose -cp lib/pdfbox.jar:lib/commons.jar: src/*.java -d bin"    # コンパイル
cmdGenerateJarFile="jar cvfm PDFConverter.jar manifest.mf -C bin ./"                 # .jarファイル生成
cmdExecutableJar="java -jar PDFConverter.jar"                                        # .jar実行

# コンパイル
function Compile()
{
    $cmdCompile
    result=$? 
}  

# .jarファイル生成
function GenerateJar()
{
    $cmdGenerateJarFile
    result=$?   
}  

#.jarファイル実行
function ExecutableJar()
{
    $cmdExecutableJar
    result=$?
}



### コマンド実行
clear


Compile
if [ reuslt = "1" ] ; then

    echo -e
    echo -e

    echo "コンパイルエラー"

    echo -e
    echo -e
else

    GenerateJar
    if [ reuslt = "1" ] ; then
        echo -e
        echo -e

        echo ".jarファイル生成エラー"

        echo -e
        echo -e 
    else
        clear
        ExecutableJar
        if [ reuslt = "1" ] ; then
            echo -e
            echo -e

            echo ".jar実行エラー"

            echo -e
            echo -e
        fi
    fi
fi        

#COMMENTOUT
