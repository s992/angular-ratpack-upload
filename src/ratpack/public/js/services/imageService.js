"use strict";

angular.module( "uploader.services" )

.service( "imageService", [ "$http", function( $http ) {
	return {

		getImages: function() {
			return $http.get( "image" );
		},

		uploadImage: function( image ) {

			var formData = new FormData();

			formData.append( "fileUpload", image );

			return $http.post( "image/upload", formData, {
				transformRequest: angular.identity,
				headers: { "Content-Type": undefined }
			});

		}

	};
}]);