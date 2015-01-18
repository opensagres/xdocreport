#! /bin/sh
#
# Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
#
# All rights reserved.
#
# Permission is hereby granted, free  of charge, to any person obtaining
# a  copy  of this  software  and  associated  documentation files  (the
# "Software"), to  deal in  the Software without  restriction, including
# without limitation  the rights to  use, copy, modify,  merge, publish,
# distribute,  sublicense, and/or sell  copies of  the Software,  and to
# permit persons to whom the Software  is furnished to do so, subject to
# the following conditions:
#
# The  above  copyright  notice  and  this permission  notice  shall  be
# included in all copies or substantial portions of the Software.
#
# THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
# EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
# MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

IN=$1
if [ -n "$IN" ]
then
   error
fi

OUT=$2
if [ -n "$OUT" ]
then
   error
fi

METADATA_FILE=$3
if [ -n "$METADATA_FILE" ]
then
   error
fi

DATA_DIR=$4
if [ -n "$DATA_DIR" ]
then
   error
fi

ERR=$5
if [ -n "$ERR" ]
then
   error
fi


cd `dirname $0`
BASE_DIR=`pwd`

for i in `ls  $BASE_DIR/../lib/*.jar`
    do
        CLASSPATH=$CLASSPATH:$i
    done

java -classpath "$CLASSPATH" fr.opensagres.xdocreport.document.tools.Main -in $IN -out $OUT -dataDir $DATA_DIR -metadataFile $METADATA_FILE -err $ERR


error()
{
  echo  "Impossible to launch XDocReport Tools."
}