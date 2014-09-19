import org.swalsh.image.ImageService
import ratpack.form.Form
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

def assetsPath = "public"
def imageDirName = "uploaded-files"
def imagePath = "$assetsPath/$imageDirName"
def thumbPath = "$imagePath/thumb"

ratpack {

	bindings {

		add new JacksonModule()

		init {
			launchConfig.baseDir.file( thumbPath ).toFile().mkdirs()
		}

	}

	handlers {

		assets assetsPath, "index.html"

		prefix("image") { ImageService imageService ->

			def baseDir = launchConfig.baseDir
			def imageDir = baseDir.file( imagePath ).toFile()
			def thumbDir = baseDir.file( thumbPath ).toFile()

			get {

				imageService.getUploadedImages( imageDir ).then {
					render json( imagePath: imageDirName, images: it )
				}

			}

			post("upload") {

				def form = parse Form
				def uploaded = form.file( "fileUpload" )

				if( imageService.isImageFile( uploaded ) ) {

					imageService.process( uploaded, imageDir, thumbDir ).then {
						render json( fileName: it.name )
					}

				} else {

					response.status(400).send "Invalid file type. Images only!"

				}

			}

		}

	}

}