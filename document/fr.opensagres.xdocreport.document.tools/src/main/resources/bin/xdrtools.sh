#! /bin/sh
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

cd `dirname $0`
BASE_DIR=`pwd`

for i in `ls  $BASE_DIR/../lib/*.jar`
    do
        CLASSPATH=$CLASSPATH:$i
    done

java -classpath "$CLASSPATH" fr.opensagres.xdocreport.document.tools.Main -in $IN -out $OUT -dataDir $DATA_DIR -metadataFile $METADATA_FILE -autoGenData true


error()
{
  echo  "Impossible to launch XDocReport Tools."
}