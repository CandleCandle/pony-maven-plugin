#!/bin/bash

set -eux

groupId="$1"
artifactId="$2"

rm -rf target/test
mkdir -p target/test

basedir="$(pwd)"

pushd src/test/pony/${groupId}/${artifactId}
ponyc --output "${basedir}/target/test" \
    --path "${basedir}/src/main/pony/"
    # dependencies as more --path options
popd
exec target/test/${artifactId}
