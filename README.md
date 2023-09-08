## AWS Java SDK V2示例

### 用途

通过Java SDK2.x版本访问AWS资源

### 配置/使用说明（详细示例见S3Demo.java）

1. IAM中创建用户，确保有对应资源的访问权限（如S3FullAccessPolicy)，并创建程序使用的AK、SK
2. 在代码中引入AWS SDK依赖
3. 使用AK、SK创建凭证
4. 使用步骤3的凭证创建Client,如S3Client
5. 使用步骤4创建的S3client操作资源，比如创建存储桶、上传/下载文件

