(function ($) {
    
    var showSuccessMessage = function () {
        $('.alert-success').css('visibility', 'visible');
        setTimeout(function () {
            $('.alert-success').css('visibility', 'hidden');
        }, 2000);
    };
    
    IndexController = function ($scope, $http) {
        $http.get('/services/base').success(function (base) {
            $scope.base = base;
        });
        
        $scope.save = function () {
            $http({
                url: '/services/base',
                method: 'POST',
                params: {base: $scope.base}
            }).success(function () {
                showSuccessMessage();
            });
        };
    };
})(jQuery);