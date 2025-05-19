'use strict';

/**
 * User/group controller.
 */
angular.module('docs').controller('UserGroup', function(Restangular, $scope, $state, $translate) {
  // Load users
  Restangular.one('user/list').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.users = data.users;
  });

  // Load groups
  Restangular.one('group').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.groups = data.groups;
  });

  // Open a user
  $scope.openUser = function(user) {
    $state.go('user.profile', { username: user.username });
  };

  // Open a group
  $scope.openGroup = function(group) {
    $state.go('group.profile', { name: group.name });
  };

  $scope.newUser = {
    username: '',
    password: '',
  };

  $scope.tmessage = {
    id: '',
    status: '',
    isShow: false,
    message: ''
  }

  $scope.requestSignup = function (user) {
    if (!user.username || !user.password) {
      $scope.tmessage.status = 'failed';
      $scope.tmessage.message = $translate.instant('usergroup.register.fillAllBlank');
      $scope.tmessage.isShow = true;
      $scope.id = 'failed';
      setTimeout(() => { if($scope.tmessage.id === 'failed'){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
      return;
    }
    Restangular.one('/signup/create').post("", {
      username: user.username,
      password: user.password,
    }).then(function(response) {
      $scope.tmessage.status = 'success';
      $scope.tmessage.message = $translate.instant('usergroup.register.success');
      $scope.tmessage.isShow = true;
      $scope.id = 'success';
      setTimeout(() => { if($scope.tmessage.id === 'success'){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
    }).catch(function(error) {
      $scope.tmessage.status = 'failed';
      $scope.tmessage.message = $translate.instant('usergroup.register.failed') + error.data?.message;
      $scope.tmessage.isShow = true;
      $scope.id = 'failed';
      setTimeout(() => { if($scope.tmessage.id === 'failed'){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
    });
  }
});