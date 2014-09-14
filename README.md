angular-ratpack-upload
======================

An example of using AngularJS and Ratpack to upload images.

Before running, update `src/ratpack/ratpack.properties` with the correct settings:

* `other.uploadDirectory`: Absolute path to the directory where files should be uploaded
* `other.imagePath`: Path to the uploadDirectory, relative to the web root. For example, if your `uploadDirectory` is `src/ratpack/public/upload`, this setting should be `upload`.