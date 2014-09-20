"use strict";

angular.module( "uploader.services" )

.service( "imageService", [ "$q", "$http", function( $q, $http ) {

	var promise = function( callback ) {

		var deferred = $q.defer();

		callback( deferred );

		return deferred.promise;

	};

	return {

		getImages: function() {
			return promise( function( deferred ) {

				$http.get( "image" )
					.success( function( data ) {
						deferred.resolve( data );
					})
					.error( function( error ) {
						deferred.reject( error );
					});

			});
		},

		uploadImage: function( image ) {
			return promise( function( deferred ) {

				var formData = new FormData();

				formData.append( "fileUpload", image );

				$http.post( "image/upload", formData, {
					transformRequest: angular.identity,
					headers: { "Content-Type": undefined }
				}).success( function( data ) {
					deferred.resolve( data );
				}).error( function( error ) {
					deferred.reject( error );
				});

			});
		}

	};
}]);