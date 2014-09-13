angular.module( "controllers", [] )

.controller( "UploadCtrl", [ "$scope", "imageService", function( $scope, imageService ) {

	$scope.images = [];
	$scope.error = "";
	$scope.uploading = false;

	var setError = function( error ) {
		$scope.error = error || "";
		$scope.uploading = false;
	};

	var clearError = function() {
		setError("");
	};

	imageService.getImages().then(
		function( data ) {
			$scope.images = data;
		},
		setError
	);

	$scope.uploadFile = function() {

		$scope.uploading = true;
		clearError();

		imageService.uploadImage( $scope.fileToUpload ).then(
			function( data ) {
				$scope.images.push( data.fileName );
				$scope.uploading = false;
			},
			setError
		);

	};

}])