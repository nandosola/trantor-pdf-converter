# PDF Image Utils

This library allows you to import scanned TIFF files and convert them into PDF 1.4 format. Additionally, a PNG thumbnail
is generated. It can import image PDFs too. As the development goes on, more image formats (GIF, PNG, JPEG) are going to
 be accepted and converted to PDF. In any case, the minimum resolution for the files to be accepted is 300dpi. Thumbnails
are generated at a resolution of 72 dpi.

For now, this is a work in progress for an internal project. However, the information on the Internet is so scarce and
spread, that this code might help you.

The main goal is achieving [PDF/a1-b](http://www.pdfa.org/2011/08/improved-pdfa-1b/) compliance in order to store these
 images in a document archive.


## Usage

1. Clone the project.
1. The dependency named `image-filters` is fetched froum our local Nexus repository. If you don't have a local Maven
repo, please download them to a known directory and change the dependency scope to `system`.
1. Execute `mvn clean package`.
1. Please find under the `target/` directory both the artifact `imageutils-1.0-SNAPSHOT.jar`. The dependencies (including
 `.so` files) are located under `target/lib/`.

The binary dependencies (`.so`s) require the following environmental variables to be set:

1. `JAIHOME` must point to the directory where `libclib_jiio.so` and `libmlib_jai.so` are located.
1. `LD_LIBRARY_PATH` must include `JAIHOME`

Thus, any application using `imageutils` should declare the variables above.

## Known issues

* Binary dependencies (`jai` & `jai_imageio`) are downloaded for `linux-amd64`. If you want to change the target platform,
 have a look at the project's `pom.xml`.
* Images smaller than DIN-A4 are stretched to fill the entire page.

## TODO

* Improve PDF image extraction.
* Generate [ISO 216-A page sizes]() dinamically.
* Generate PDF/A-1b from scanned image PDF
  * Insert RDF & metadata into target PDFs.
* Deploy as a Maven artifact.
* JavaDocs.
* Tests:
  * [Images](http://en.wikipedia.org/wiki/Standard_test_image)
  * [PDF](https://duckduckgo.com/?q=testing+pdf+generation)

## Licenses

* GNU Lesser General Public License, version 3. See LICENSE file for the whole enchilada.
* See the `licenses/` directory for additional software licenses and terms of service notices.