# PDF Image Utils

This library allows you to import scanned TIFF files and convert them into PDF 1.4 format. Additionally, a PNG thumbnail
is generated. It can import image PDFs too. As the development goes on, more image formats (GIF, PNG, JPEG) are going to
 be accepted and converted to PDF. In any case, the minimum resolution for the files to be accepted is 300dpi. Thumbnails
are generated at a resolution of 72 dpi.

For now, this is a work in progress for an internal JRuby project. However, the information on the Internet is so scarce and
spread, that this code might help you.

The main goal is achieving [PDF/a1-b](http://www.pdfa.org/2011/08/improved-pdfa-1b/) compliance in order to store these
 images in a document archive.


## Usage

1. Make sure you have at least Open/Oracle JDK 1.7.0_15
1. Clone the project.
1. The dependency named `image-filters` is fetched froum our local Nexus repository. If you don't have a local Maven
repo, please download them to a known directory and change the dependency scope to `system`.
1. Execute `mvn clean install`.

* The JRuby gem is ready to be generated from `target/ruby-gem`. There is [an experimental gem](https://rubygems.org/gems/imageutils) already deployed at RubyGems
* For Java users: the artifact is located under the `target/` directory. Its dependencies are located under `target/ruby-gem/lib`.
 The binary extensions and their JNI adapters are located under `target/ruby-gem/lib`.

Note that, if you want to use the artifacts from pure Java, the binary dependencies (`.so` files) require the following environmental variables to be set:

1. `JAIHOME` must point to the directory where `libclib_jiio.so` and `libmlib_jai.so` are located.
1. Add `JAIHOME` to your `CLASSPATH` too.
1. `LD_LIBRARY_PATH` (or `LD_RUN_PATH` if not executed from commandline or script) must include `JAIHOME`.

Thus, any Java application using `imageutils` should declare the variables above. The JRuby gem declares the variables above automatically when required.

## Known issues

* AFAIK, [there is a bug](https://github.com/rubygems/rubygems/issues/507) up to JRuby 1.7.3 that prevents pushing gems to RubyGems.
 So if you want to publish your own gem, at least 1.7.4-dev should be used. In my case, the gem was pushed via MRI 1.9.3.
* PDF conversion tests will fail under OS X with JDK7. See [this post](http://stackoverflow.com/questions/12407479/why-does-files-probecontenttype-return-null) on StackOverflow.
* On OS X, it might me necessary to create the symlink: `soffice.bin -> ./soffice` under `/Applications/LibreOffice.app/Contents/MacOS`. Just execute the integration tests and check the error messages.

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
