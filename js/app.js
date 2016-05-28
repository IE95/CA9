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
        this.depositRequests = []
        this.foundUserProfile = null ;

        this.seeProfilePermission = false;
        this.depositRequestPermission = false;
        this.seeTradeInfoPermission = false;
        this.confirmDepositRequestPermission = false;
        this.seeMarketStatusPermission = false;
        this.tradePermission = false;
        this.addStockPermission = false;
        this.seeRecordsPermission = false;
        this.seeUsersProfilePermission = false;
        this.confirmPendingStockPermission = false;
        this.confirmExpensiveTradePermission = false;
        this.setTradeLimitPermission = false;
        this.setStockStatsPermission = false;
        this.csrfToken = null;

        $scope.reqErrorMessage = "" ;
        $scope.loginErrorMessage = "" ;
        $scope.loadStocksErrorMessage = "" ;
        $scope.loadRecordsErrorMessage = "" ;
        $scope.depositRequestResMsg = "" ;
        $scope.loadDepositRequestsErrMsg = "" ;
        $scope.loadUserProfileErrMsg = "" ;
        $scope.addRoleResMsg = "" ;
        $scope.getBackupResMsg = "" ;
        $scope.signupResMsg = "" ;

         this.config = 
        {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        }

        this.hasRole = function(role){
            for (var i = 0; i < boorsCtrl.user.roles.length; i++) {
                if(boorsCtrl.user.roles[i] === role)
                    return true;
            };
            return false;
        }

        this.setPermissions = function(){
            boorsCtrl.seeProfilePermission = true;
            boorsCtrl.seeMarketStatusPermission = true;
            if(boorsCtrl.hasRole('user')){
                boorsCtrl.depositRequestPermission = true;
                boorsCtrl.seeTradeInfoPermission = true;
                boorsCtrl.tradePermission = true;
            }
            if(boorsCtrl.hasRole('finance_user')){
                boorsCtrl.confirmDepositRequestPermission = true;
                boorsCtrl.confirmExpensiveTradePermission = true;
            }
            if(boorsCtrl.hasRole('company_owner')){
                boorsCtrl.addStockPermission = true;
                boorsCtrl.setStockStatsPermission = true;                
            }
            if(boorsCtrl.hasRole('admin')){
                boorsCtrl.depositRequestPermission = true;
                boorsCtrl.seeTradeInfoPermission = true;
                boorsCtrl.confirmDepositRequestPermission = true;
                boorsCtrl.tradePermission = true;
                boorsCtrl.addStockPermission = true;
                boorsCtrl.seeRecordsPermission = true;
                boorsCtrl.seeUsersProfilePermission = true;
                boorsCtrl.confirmPendingStockPermission = true;
                boorsCtrl.confirmExpensiveTradePermission = true;
                boorsCtrl.setTradeLimitPermission = true;
                boorsCtrl.setStockStatsPermission = true;                
            }            
        }

        this.getUserInfo = function(){
            $http.get('/boors/UserInfoHandler?csrf='+boorsCtrl.csrfToken).success(function(response) {
                if(response.result === 1){
                    boorsCtrl.user = response.userInfo;
                    if(boorsCtrl.hasRole('admin')){
                        boorsCtrl.getRecords();
                    }
                    if(boorsCtrl.hasRole('admin') || boorsCtrl.hasRole('finance_user')){
                        boorsCtrl.getDepositRequests();                    
                    }
                    if(boorsCtrl.hasRole('admin')){
                        boorsCtrl.getPendingStocks();
                    }
                    if(boorsCtrl.hasRole('admin') || boorsCtrl.hasRole('finance_user')){
                        boorsCtrl.getExpensiveReq();
                    }
                    if(boorsCtrl.hasRole('admin') || boorsCtrl.hasRole('company_owner')){
                        boorsCtrl.getUserStocks();
                    }
                    boorsCtrl.setPermissions();
                }
            }); 
        };

        this.getStockList = function(stockId){
            $http.get('/boors/StockQueueHandler?instrument='+stockId+"&csrf="+boorsCtrl.csrfToken).success(function(response) {
                if(response.result === 1){
                    boorsCtrl.stocks = response.stocks;
                }else{
                    $scope.loadStocksErrorMessage = response.errMsg ;
                }
            });
        };

        this.getRecords = function(){
            $http.get('/boors/RecordsHandler?csrf='+boorsCtrl.csrfToken).success(function(response) {                
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
            $http.get('/boors/DoTrade?id='+boorsCtrl.user.id+'&instrument='+boorsCtrl.selectedStock+'&price='+$scope.price+'&quantity='+$scope.quantity+'&opType='+$scope.method+'&tradeType='+type+"&csrf="+boorsCtrl.csrfToken).success(function(response) {
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
                    $scope.latestTrans = response.messages;
                    angular.element("#RequestTrade").modal("hide")
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

        this.getPendingStocks = function(){
            $http.get('/boors/getPendingStock?csrf='+boorsCtrl.csrfToken)
            .success(function(stocksData) {
                boorsCtrl.pendingStocks = stocksData;
            }).error(function(data, status) {
                alert("error" +status);
            });   
        };       

        this.addNewStock = function(){

            var data = $.param({
                name: $scope.newPendingStock,
                quantity: $scope.pendingQuantity,
                price: $scope.pendingPrice,
                type: $scope.pendingType,
                csrf : boorsCtrl.csrfToken

            });
            
            $http.post('/boors/addNewPendingStock',data,boorsCtrl.config
                ).success(function(response){
                    $scope.stockResponse = response;
                    if(boorsCtrl.hasRole('admin')){
                        boorsCtrl.getPendingStocks();
                    }
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
                opType: order.opType,
                csrf : boorsCtrl.csrfToken
            });
            
            $http.post('/boors/ConfirmSymbol',data,boorsCtrl.config
                ).success(function(response){
                    if(boorsCtrl.hasRole('admin')){
                        boorsCtrl.getPendingStocks();
                    }
                }).error(function(error){
                    alert(error);   
            });
        };

        this.setLimitation = function(){
            var data = $.param({
                limit: $scope.transactionLimitation,
                csrf : boorsCtrl.csrfToken
            });
            $http.post('/boors/SetLimit',data,boorsCtrl.config
                ).success(function(response){
                    $scope.limitResponse = response;
                }).error(function(error){
                    alert(error);   
            });
        };

        this.getExpensiveReq=function(){
            $http.get('/boors/GetExpensive?csrf='+boorsCtrl.csrfToken)
            .success(function(response) {
                boorsCtrl.expensiveRequests = response;
            }).error(function(data, status) {
                alert("error" +status);
            });   
        };

        this.getUserStocks=function(){
             $http.get('/boors/StockStatistic?csrf='+boorsCtrl.csrfToken)
            .success(function(response) {
                boorsCtrl.userStocks = response;
            }).error(function(data, status) {
                alert("error" +status);
            });
        };
        
        this.confirmExpensive = function(order){
            var data = $.param({
                id: order.id,
                csrf : boorsCtrl.csrfToken
            });
            $http.post('/boors/ConfirmExpensive',data,boorsCtrl.config
                ).success(function(response){
                    $scope.expensiveResponse = response
                    if(boorsCtrl.hasRole('admin') || boorsCtrl.hasRole('finance_user')){
                        boorsCtrl.getExpensiveReq()
                    }
                }).error(function(error){
                    alert(error);   
            });

            $http.get('/boors/DoTrade?id='+order.userId+'&instrument='+order.symbol+'&price='+order.price+'&quantity='+order.quan+'&opType='+order.opType+'&tradeType='+order.type+'&notCheck='+'yes'+"&csrf="+boorsCtrl.csrfToken).success(function(response) {
                
            }); 

            
        };

        this.depositRequest = function(){
            var data = $.param({
                amount: $scope.depositAmount,
                csrf : boorsCtrl.csrfToken
            });
            
            $http.post('/boors/AddDepositRequest',data,boorsCtrl.config
                ).success(function(response){
                    if(response.result === 1){
                        boorsCtrl.getUserInfo();
                    }
                    else
                        $scope.depositRequestResMsg=response.message;
                    $scope.depositAmount = "";
                }).error(function(error){
                    $scope.depositRequestResMsg = error;
                    $scope.depositAmount = "";
            });
            $scope.newPendingStock = ""
            if(boorsCtrl.hasRole('admin')){
                boorsCtrl.getPendingStocks();            
            }
        }

        this.responseDepositRequest = function(requestId,response){
            var data = $.param({
                requestId: requestId,
                command: response,
                csrf : boorsCtrl.csrfToken
            });
            
            $http.post('/boors/ResponseDepositRequest',data,boorsCtrl.config
                ).success(function(response){
                    if(response.result === 1){
                        boorsCtrl.getUserInfo();
                    }else{
                        alert(response.message)
                    }
                }).error(function(error){
                    alert(error);
            });
        }

        this.getDepositRequests = function(){
            $http.get('/boors/GetDepositRequests?csrf='+boorsCtrl.csrfToken).success(function(response) {                
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
            $http.get('/boors/GetUserProfile?userId=' + userId+"&csrf="+boorsCtrl.csrfToken).success(function(response) {
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

        this.addUserRole = function(role){
            var data = $.param({
                userId: boorsCtrl.foundUserProfile.id,
                role: role,
                csrf : boorsCtrl.csrfToken
            });
            
            $http.post('/boors/AddUserRole',data,boorsCtrl.config
                ).success(function(response){
                    if(response.result === 1){
                        $scope.addRoleResMsg = "successfull";
                    }else{
                        $scope.addRoleResMsg = response.message;
                    }
                    $scope.selectedRole = null ;
                }).error(function(error){                
                    $scope.addRoleResMsg = error ;
                    $scope.selectedRole = null ;
            });            
        }

        this.getBackup = function(){
            $http.get('/boors/CSVExporter?csrf='+boorsCtrl.csrfToken).success(function(response) {
                $scope.getBackupResMsg = response.message;
            }).error(function(error){
                $scope.loadUserProfileErrMsg = error;
            });            
        }


        this.getCSRF=function(){
            $http.get('/boors/getCSRF').success(function(response) {
                boorsCtrl.csrfToken = response;
            }).error(function(error){
                
            });   
        };
        this.getCSRF();

        this.signup = function(){
            if($scope.userId === undefined || $scope.name === undefined || $scope.family === undefined || 
                $scope.email === undefined || $scope.password ===undefined || $scope.repeatPassword === undefined ||
                $scope.userId === "" || $scope.name === "" || $scope.family === "" || 
                $scope.email === "" || $scope.password ==="" || $scope.repeatPassword === ""){
                    
                $scope.signupResMsg = "fill all field" ;
            }else if($scope.password !== $scope.repeatPassword){
                $scope.signupResMsg = "password doesn't match" ;
            }else{
                var data = $.param({
                id: $scope.userId,
                name: $scope.name,
                family: $scope.family,
                password: $scope.password , 
                email: $scope.email,
                csrf: boorsCtrl.csrfToken
                 });
                $http.post('/boors/UserAdder',data,boorsCtrl.config
                    ).success(function(response){
                        if(response.result === 1){
                            $scope.signupResMsg = "user successfully added" ;
                            $scope.userId = "";
                            $scope.name = "";
                            $scope.family = "";
                            $scope.email = "";
                            $scope.password = "";
                            $scope.repeatPassword = "";
                        }else{
                            $scope.signupResMsg = response.message;
                        }
                    }).error(function(error){                
                        $scope.signupResMsg = error ;
                });            
            }

        }

        
        $timeout(function(){boorsCtrl.getUserInfo()}, 1000);
        $timeout(function(){boorsCtrl.getStockList("all")}, 1000);
 
        
        stop = $interval(function() {boorsCtrl.getStockList("all");}, 15000);

    }]);
})();
