
Example JSON


```json
{
  "id" : 1,
  "name" : "Josiah Imani",
  "email" : "jImani@cloudNativeData.io"
}
```


```python
from pyspark.sql import SparkSession

# 1. Initialize SparkSession with Iceberg and S3A (MinIO) configurations
spark = SparkSession.builder \
    .appName("Iceberg MinIO Read") \
    .config("spark.jars.packages", 
            "org.apache.iceberg:iceberg-spark-runtime-3.4_2.12:1.3.1,"
            "org.apache.hadoop:hadoop-aws:3.3.4,"
            "com.amazonaws:aws-java-sdk-bundle:1.12.262") \
    .config("spark.sql.extensions", 
            "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions") \
    .config("spark.sql.catalog.local", "org.apache.iceberg.spark.SparkCatalog") \
    .config("spark.sql.catalog.local.type", "hadoop") \
    .config("spark.sql.catalog.local.warehouse", "s3a://iceberg-bucket/warehouse") \
    .config("spark.hadoop.fs.s3a.aws.credentials.provider", 
            "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider") \
    .config("spark.hadoop.fs.s3a.endpoint", "http://s3:9000") \
    .config("spark.hadoop.fs.s3a.access.key", "admin") \
    .config("spark.hadoop.fs.s3a.secret.key", "password123") \
    .config("spark.hadoop.fs.s3a.path.style.access", "true") \
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem") \
    .getOrCreate()

# Query the table
df = spark.read \
    .format("iceberg") \
    .load("s3a://iceberg-bucket/warehouse/default_db/users")

df.show()

# -----------------------------------------------------------------------------
# Approach 1: Read using Catalog Namespace notation (local.default_db.users)
# -----------------------------------------------------------------------------
print("--- Reading via Iceberg Catalog ---")
df_catalog = spark.read.table("local.default_db.users")
df_catalog.printSchema()
df_catalog.show(truncate=False)

# -----------------------------------------------------------------------------
# Approach 2: Read directly using the S3 Path as an Iceberg format table
# -----------------------------------------------------------------------------
print("--- Reading directly via S3 Path ---")
df_path = spark.read \
    .format("iceberg") \
    .load("s3a://iceberg-bucket/warehouse/default_db/users")

df_path.show(truncate=False)

# -----------------------------------------------------------------------------
# Approach 3: Query using Spark SQL
# -----------------------------------------------------------------------------
print("--- Querying using Spark SQL ---")
df_sql = spark.sql("""
    SELECT * 
    FROM local.default_db.users 
    WHERE id IS NOT NULL
""")
df_sql.show(truncate=False)

# Stop session
spark.stop()
```