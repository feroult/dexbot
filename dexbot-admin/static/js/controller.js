(function ($) {
    
    var showSuccessMessage = function () {
        $('.alert-success').css('visibility', 'visible');
        setTimeout(function () {
            $('.alert-success').css('visibility', 'hidden');
        }, 2000);
    };

    app = angular.module('dexbot', []);
    
    app.config(function($routeProvider) {
        $routeProvider
                    .when('/list', {controller: ListController, templateUrl:'partials/list.html'})
                    .when('/crud/:id', {controller: CrudController, templateUrl:'partials/crud.html'})
                    .otherwise({
                        redirectTo: '/list'
                    });
        });
    
    ListController = function ($scope, $http) {
        $http.get('/services/base/list').success(function (bases) {
            $scope.bases = bases;
        });
        
        $scope.add = function() {
            
            $http({
                method : 'POST',
                url : '/services/base/list',
                data : $.param({desc: $scope.newDesc}),
                headers : {
                    'Content-Type' : 'application/x-www-form-urlencoded'
                    }
            }).success(function (id) {
                location.hash = '/crud/' + id;
            });
        }
    };

    CrudController = function ($scope, $http, $routeParams) {
        $http.get('/services/base?id=' + $routeParams.id).success(function (base) {
            $scope.base = base;
        });

        $scope.save = function () {
            $http({
                url: '/services/base',
                method: 'PUT',
                params: {base: JSON.stringify($scope.base)}
            }).success(function () {
                showSuccessMessage();
            });
        };
    };
})(jQuery);