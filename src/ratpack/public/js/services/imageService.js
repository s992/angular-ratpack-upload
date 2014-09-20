angular.module( "uploader.services" )

.service( "imageService", [ "$q", "$http", function( $q, $http ) {
	return {

		getImages: function() {

			var deferred = $q.defer();

			$http.get( "image" )
				.success( function( data ) {
					deferred.resolve( data );
				})
				.error( function( error ) {
					deferred.reject( error );
				});

			return deferred.promise;

		},

		uploadImage: function( image ) {

			var deferred = $q.defer(),
				formData = new FormData();

			formData.append( "fileUpload", image );

			$http.post( "image/upload", formData, {
				transformRequest: angular.identity,
				headers: { "Content-Type": undefined }
			}).success( function( data ) {
				deferred.resolve( data );
			}).error( function( error ) {
				deferred.reject( error );
			});

			return deferred.promise;

		}

	};
}])