#! /bin/sh
BASE_ADDRESS=$1
if [ -n "$BASE_ADDRESS" ]
then
   error
fi

USER=$2
if [ -n "$USER" ]
then
   error
fi

PASSWORD=$3
if [ -n "$PASSWORD" ]
then
   error
fi

SERVICE_TYPE=$4
if [ -n "$SERVICE_TYPE" ]
then
   error
fi

SERVICE_NAME=$5
if [ -n "$SERVICE_NAME" ]
then
   error
fi

OUT=$6
if [ -n "$OUT" ]
then
   error
fi

ERR=$7
if [ -n "$ERR" ]
then
   error
fi

RESOURCES=$8
if [ -n "$RESOURCES" ]
then
   error
fi

CHUNK=$9
if [ -n "$CHUNK" ]
then
   error
fi

TIMEOUT=$10
if [ -n "$TIMEOUT" ]
then
   error
fi

cd `dirname $0`
BASE_DIR=`pwd`

for i in `ls  $BASE_DIR/../lib/*.jar`
    do
        CLASSPATH=$CLASSPATH:$i
    done

java -classpath "$CLASSPATH" fr.opensagres.xdocreport.document.tools.remoting.resources.Main -baseAddress $BASE_ADDRESS -user $USER -password $PASSWORD -serviceType $SERVICE_TYPE -serviceName $SERVICE_NAME -out $OUT -err $ERR -resources $RESOURCES -chunk $CHUNK -timeout $TIMEOUT


error()
{
  echo  "Impossible to launch XDocReport-Repository Tools."
}
