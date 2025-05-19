'use strict';

/**
 * Controller: RegisterList
 * Description: Implements the "New user request" feature,
 *              including fetching, filtering, sorting, approving,
 *              and rejecting user registration requests.
 * Reference: Inspired by implementation in the repository:
 *            https://github.com/Jayfeather233/Teedy
 */

angular.module('docs').controller('RegisterList', function(Restangular, $scope, $translate) {

  $scope.tmessage = {
    id: '',
    status: '',
    isShow: false,
    message: ''
  }
  $scope.filterStatus = "all";
  $scope.filtered_list = [];
  $scope.sortColumn = 'create_date';
  $scope.reverseSort = true;
  $scope.loadData = function() {
    Restangular.all('/signup/all').getList().then(function(response) {
      $scope.all_list = response;
      $scope.filterByStatus($scope.filterStatus);
    }).catch(function(error) {
      console.error("Error fetch signup list:", error);
      alert("加载注册请求列表失败");
    });
  }
  $scope.loadData();

  $scope.sortData = function(column) {
    if ($scope.sortColumn === column) {
      $scope.reverseSort = !$scope.reverseSort;
    } else {
      $scope.sortColumn = column;
      $scope.reverseSort = false;
    }
  };

  $scope.filterByStatus = function(status) {
    $scope.filterStatus = status;
    if (status === 'all') {
      $scope.filtered_list = $scope.all_list;
    } else {
      $scope.filtered_list = $scope.all_list.filter(function(item) {
        if (status === 'accepted') {
          return item.accept_date !== 'null';
        } else if (status === 'rejected') {
          return item.reject_date !== 'null';
        } else if (status === 'pending') {
          return item.accept_date === 'null' && item.reject_date === 'null';
        }
      });
    }
  };
  $scope.approve = function(item) {
    Restangular.all('/signup/accept').post("", { id: item.id })
        .then(function(response) {
          $scope.tmessage.status = 'success';
          $scope.tmessage.message = $translate.instant('register.status.success');
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;
          setTimeout(() => { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
          $scope.loadData();
        })
        .catch(function(error) {

          $scope.tmessage.status = 'failed';
          $scope.tmessage.message = $translate.instant('register.status.failed') + error;
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;
          setTimeout(() => { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
        });
  };
  $scope.reject = function(item) {
    Restangular.all('/signup/reject').post("", { id: item.id })
        .then(function(response) {
  
          $scope.tmessage.status = 'success';
          $scope.tmessage.message = $translate.instant('register.status.success');
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(() => { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
          $scope.loadData();
        })
        .catch(function(error) {

          $scope.tmessage.status = 'failed';
          $scope.tmessage.message = $translate.instant('register.status.failed') + error;
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(() => { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
        });
  };

});