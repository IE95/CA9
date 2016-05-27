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
        this.pendingStocks = [];
        this.searchInput = ""
        this.expensiveRequests = [];
        this.userStocks = [];

        $scope.reqErrorMessage = "" ;
        $scope.loginErrorMessage = "" ;
        $scope.loadStocksErrorMessage = "" ;
        $scope.loadRecordsErrorMessage = "" ;

         this.config = 
        {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        }


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
                    angular.element("#RequestTrade").modal("hide")
                    boorsCtrl.getExpensiveReq();
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


        this.getPendingStocks = function(){
            $http.get('/boors/getPendingStock')
            .success(function(stocksData) {
                boorsCtrl.pendingStocks = stocksData;
            }).error(function(data, status) {
                alert("error" +status);
            });   
        };

        this.getPendingStocks();
       

        this.addNewStock = function(){

            var data = $.param({
                name: $scope.newPendingStock,
                quantity: $scope.pendingQuantity,
                price: $scope.pendingPrice,
                type: $scope.pendingType
            });
            
            $http.post('/boors/addNewPendingStock',data,boorsCtrl.config
                ).success(function(response){
                    $scope.stockResponse = response;
                    boorsCtrl.getPendingStocks();
                }).error(function(error){
                    alert(error);   
            });

            $scope.newPendingStock = ""
            $scope.pendingQuantity = ""
            $scope.pendingPrice = ""
            $scope.pendingType = ""

           
        };

        this.confirmStock = function(order){
            var data = $.param({
                name: order.symbol,
                ownerId : order.userId,
                quantity: order.quan,
                price: order.price,
                orderType: order.type,
                opType: order.opType
            });
            
            $http.post('/boors/confirmSymbol',data,boorsCtrl.config
                ).success(function(response){
                    boorsCtrl.getPendingStocks();
                }).error(function(error){
                    alert(error);   
            });
        };

        this.setLimitation = function(){
            var data = $.param({
                limit: $scope.transactionLimitation
            });
            $http.post('/boors/setLimit',data,boorsCtrl.config
                ).success(function(response){
                    $scope.limitResponse = response;
                }).error(function(error){
                    alert(error);   
            });
        };

        this.getExpensiveReq=function(){
            $http.get('/boors/getExpensiveRequests')
            .success(function(response) {
                boorsCtrl.expensiveRequests = response;
            }).error(function(data, status) {
                alert("error" +status);
            });   
        };

        this.getUserStocks=function(){
             $http.get('/boors/getMyStockStatistic')
            .success(function(response) {
                boorsCtrl.userStocks = response;
            }).error(function(data, status) {
                alert("error" +status);
            });
        }();
        
        this.confirmExpensive = function(order){
            var data = $.param({
                id: order.id
            });
            $http.post('/boors/confirmExpensive',data,boorsCtrl.config
                ).success(function(response){
                    $scope.expensiveResponse = response
                    boorsCtrl.getExpensiveReq()
                }).error(function(error){
                    alert(error);   
            });

            $http.get('http://localhost:8080/boors/dotrade?id='+order.userId+'&instrument='+order.symbol+'&price='+order.price+'&quantity='+order.quan+'&opType='+order.opType+'&tradeType='+order.type+'&notCheck='+'yes').success(function(response) {
                
            }); 

            
        };

        this.getExpensiveReq();
        this.getUserInfo();

        this.getStockList("all");
        stop = $interval(function() {boorsCtrl.getStockList("all");}, 15000);

    }]);
})();
