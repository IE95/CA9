(function() {
    
    var app = angular.module('boors', ['ngMessages']);	

    app.controller('BoorsController', ['$scope', '$http', '$timeout','$interval', function($scope, $http, $timeout,$interval){
        var boorsCtrl = this;
        this.user = {
            id : null
        };
        this.stocks = [] ;
        this.selectedStock = "";
        this.records = [] ;
        this.searchInput = ""

        $scope.reqErrorMessage = "" ;
        $scope.loginErrorMessage = "" ;
        $scope.loadStocksErrorMessage = "" ;
        $scope.loadRecordsErrorMessage = "" ;



        this.getUserInfo = function(){
            if(boorsCtrl.user.id !==null){
                $http.get('http://localhost:8080/boors/userinfo?id='+boorsCtrl.user.id).success(function(response) {
                    if(response.result === 1){
                        boorsCtrl.user = response.userInfo;
                    }
                }); 
            }          
        };

        this.getStockList = function(stockId){
            $http.get('http://localhost:8080/boors/stockQueue?instrument='+stockId).success(function(response) {
                if(response.result === 1){
                    boorsCtrl.stocks = response.stocks;
                }else{
                    $scope.loadStocksErrorMessage = response.errMsg ;
                }
            });
        };

        this.getRecords = function(){
            $http.get('http://localhost:8080/boors/records').success(function(response) {                
                if(response.result === 1){
                    boorsCtrl.records = response.records;
                }else{
                    $scope.loadRecordsErrorMessage = response.errMsg ;
                }
            });
        }

        this.selectStock = function(stockId){
            boorsCtrl.selectedStock = stockId;
        };

        this.doTrade = function(type){
            $http.get('http://localhost:8080/boors/dotrade?id='+boorsCtrl.user.id+'&instrument='+boorsCtrl.selectedStock+'&price='+$scope.price+'&quantity='+$scope.quantity+'&opType='+$scope.method+'&tradeType='+type).success(function(response) {
                if(response.result === 0){
                    $scope.reqErrorMessage = response.messages[0];
                }
                else{
                    $scope.price = null;
                    $scope.quantity = null;
                    $scope.method = null;
                    $scope.reqErrorMessage = "";
                    boorsCtrl.getUserInfo();
                    boorsCtrl.getStockList("all");
                    if(boorsCtrl.user.id === 1)
                        boorsCtrl.getRecords();
                    $scope.latestTrans = response.messages;
                    angular.element("#RequestTrade").modal("hide");
                }
            }); 

        }

        this.hasShare = function(){
            for(var i =0;i<boorsCtrl.user.shares.length;i++){
                if(boorsCtrl.user.shares[i].name === selectedStock){
                    return true;
                }
            }
            return false;
        }

        this.refresh = function(){
            $scope.latestTrans = [];
            boorsCtrl.getStockList("all");
        }

        this.login = function(){
            $http.get('http://localhost:8080/boors/userinfo?id='+$scope.loginId).success(function(response) {
                if(response.result === 1){
                    boorsCtrl.user = response.userInfo;
                    $scope.loginErrorMessage="";
                    $scope.loginId = "";
                    if(boorsCtrl.user.id === 1)
                        boorsCtrl.getRecords();
                }else{
                    $scope.loginErrorMessage=response.errMsg;
                    $scope.loginId = "";
                }
            }); 
        }

        this.getStockList("all");
        stop = $interval(function() {boorsCtrl.getStockList("all");}, 15000);

    }]);
})();
