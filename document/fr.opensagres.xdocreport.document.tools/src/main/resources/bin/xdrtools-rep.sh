#! /bin/sh
PARAM=$1
if [ -n "$PARAM" ]
then
   error
fi

cd `dirname $0`
BASE_DIR=`pwd`

for i in `ls  $BASE_DIR/../lib/*.jar`
    do
        CLASSPATH=$CLASSPATH:$i
    done

java -classpath "$CLASSPATH" fr.opensagres.xdocreport.document.tools.remoting.resources.Main $PARAM

error()
{
  echo  "Impossible to launch XDocReport-Repository Tools."
}
