#!/bin/sh
set -eu
VERSION=3.10.0
BASE="${HOME}/.m2/wrapper/dists"
MVN_HOME="${BASE}/apache-maven-${VERSION}"
if [ ! -x "${MVN_HOME}/bin/mvn" ]; then
  mkdir -p "${BASE}"
  ZIP="${BASE}/apache-maven-${VERSION}-bin.zip"
  URL="https://archive.apache.org/dist/maven/maven-3/${VERSION}/binaries/apache-maven-${VERSION}-bin.zip"
  curl --fail --location --silent --show-error "${URL}" -o "${ZIP}"
  EXPECTED="1f6d9909266510f039f59aa0e13dcd2c66da85f043e56276e41f2918f8bddaff"
  if command -v sha256sum >/dev/null 2>&1; then
    ACTUAL="$(sha256sum "${ZIP}" | awk '{print tolower($1)}')"
  else
    ACTUAL="$(shasum -a 256 "${ZIP}" | awk '{print tolower($1)}')"
  fi
  [ "${EXPECTED}" = "${ACTUAL}" ] || { echo "Maven checksum mismatch" >&2; exit 1; }
  (cd "${BASE}" && jar xf "${ZIP}")
  mv "${BASE}/apache-maven-${VERSION}" "${MVN_HOME}"
fi
exec "${MVN_HOME}/bin/mvn" "$@"
