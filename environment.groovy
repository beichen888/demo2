environments {
    dev {
        jdbc {
           driver = "com.mysql.jdbc.Driver"
           url = "jdbc:mysql://localhost:3306/demo2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull"
           username = "root"
           password = "123456"
        }

        pool {
           maxActive = 40
           maxIdle = 10
        }

        mail{
            host = "smtp.163.com"
            username = "miguo321@163.com"
            password = "xxx"
        }

        project{
            domain = "http://localhost:8090/"
            uploadFolder = "/upload"
            tmpFolder = "/upload"
        }

        server{
            port = 8090
            env = "dev"
        }
    }
    
    production {
        jdbc {
            driver = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://rm-demo2.mysql.rds.aliyuncs.com:3306/demo2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull"
            username = "demo2"
            password = "demo2"
        }

        pool {
            maxActive = 40
            maxIdle = 10
        }

        mail{
            host = "smtp.163.com"
            username = "demo2@163.com"
            password = "xxx"
        }

        project{
            domain = "https://www.demo2.cn/"
            uploadFolder = "/usr/share/nginx/demo2/frontend/upload"
            tmpFolder = "/usr/share/nginx/demo2/frontend/tmp"
        }

        server{
            port = 8090
            env = "prod"
        }
    }
}
