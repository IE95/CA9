(function() {
    
    var app = angular.module('myboors', ['ngMessages']);	

    app.controller('myBoorsController', ['$scope', '$http', '$timeout','$interval', function($scope, $http, $timeout,$interval){
        var boorsCtrl = this;
        this.user = {
            id : null
        };
        this.stocks = [] ;
        this.selectedStock = "";
        this.records = [] ;
        this.pendingStocks = [];
        this.searchInput = ""
////////////////////////sss
        this.depositRequests = []
        this.foundUserProfile = null ;
////////////////////////fff


        $scope.reqErrorMessage = "" ;
        $scope.loginErrorMessage = "" ;
        $scope.loadStocksErrorMessage = "" ;
        $scope.loadRecordsErrorMessage = "" ;
//////////////////////////// sssss
        $scope.depositRequestResMsg = "" ;
        $scope.loadDepositRequestsErrMsg = "" ;
        $scope.loadUserProfileErrMsg = "" ;
/////////////////////// fffff


         this.config = 
        {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        }

////////////////////////sssss
        this.getUserInfo = function(){
            $http.get('http://localhost:8080/boors/userinfo').success(function(response) {
                if(response.result === 1){
                    boorsCtrl.user = response.userInfo;
                    if(boorsCtrl.user.id === 1){
                        boorsCtrl.getRecords();
                        boorsCtrl.getDepositRequests();
                    }
                }
            }); 
        };
///////////////////////fffff
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

///////////////////// sssss remove
        this.login = function(){
            $http.get('http://localhost:8080/boors/userinfo').success(function(response) {
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
/////////////////////fffffff

        this.getPendingStocks = function(){
            $http.get('/boors/getPendingStock')
            .success(function(stocksData) {
                boorsCtrl.pendingStocks = stocksData;
            }).error(function(data, status) {
                alert("error" +status);
            });   
        }();
       

        this.addNewStock = function(){

            var data = $.param({
                name: $scope.newPendingStock
            });
            
            $http.post('/boors/addNewPendingStock',data,boorsCtrl.config
                ).success(function(response){
                    $scope.stockResponse = response;
                }).error(function(error){
                    alert(error);   
            });

            $scope.newPendingStock = ""
            boorsCtrl.getPendingStocks();
        }


/////////////////////////////////sssss
        this.depositRequest = function(){
            var data = $.param({
                amount: $scope.depositAmount
            });
            
            $http.post('/boors/addDepositRequest',data,boorsCtrl.config
                ).success(function(response){
                    if(response.result === 1)
                        boorsCtrl.getUserInfo();
                    else
                        $scope.depositRequestResMsg=response.message;
                    $scope.depositAmount = "";
                }).error(function(error){
                    $scope.depositRequestResMsg = error;
                    $scope.depositAmount = "";
            });
            $scope.newPendingStock = ""
            boorsCtrl.getPendingStocks();            
        }

        this.responseDepositRequest = function(requestId,response){
            var data = $.param({
                requestId: requestId,
                command: response
            });
            
            $http.post('/boors/responseDepositRequest',data,boorsCtrl.config
                ).success(function(response){
                    if(response.result === 1){
                        boorsCtrl.getUserInfo();
                        boorsCtrl.getDepositRequests();
                    }else{
                        alert(response.message)
                    }
                }).error(function(error){
                    alert(error);
            });
        }

        this.getDepositRequests = function(){
            $http.get('http://localhost:8080/boors/getDepositRequests').success(function(response) {                
                if(response.result === 1){
                    boorsCtrl.depositRequests = response.depositRequests;
                    $scope.loadDepositRequestsErrMsg = "" ;
                }else{
                    $scope.loadDepositRequestsErrMsg = response.errMsg ;
                }
            }).error(function(error){
                $scope.loadDepositRequestsErrMsg = error ;
            });
        }

        this.getUserProfile = function(userId){            
            $http.get('http://localhost:8080/boors/getUserProfile?userId=' + userId).success(function(response) {
                if(response.result === 1){
                    boorsCtrl.foundUserProfile = response.userProfile;
                    $scope.loadUserProfileErrMsg = "" ;
                }else{
                    $scope.loadUserProfileErrMsg = response.errMsg;
                }
                $scope.searchUserId = "";
            }).error(function(error){
                $scope.loadUserProfileErrMsg = error;
                $scope.searchUserId = "";

            }); 
        }


        this.getUserInfo();
//////////////////////////////ffffff
        this.getStockList("all");
        stop = $interval(function() {boorsCtrl.getStockList("all");}, 15000);

    }]);
})();
