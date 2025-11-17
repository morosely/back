mvn clean
mvn archetype:create-from-project
cd  target/generated-sources/archetype
# 修改pom.xml
open "/Applications/Visual Studio Code.app" --args /Users/qianhb/omni/newomni/ftMicroNormal/target/generated-sources/archetype/pom.xml
=======================================================================================================================================
    <groupId>com.efuture</groupId>
    <artifactId>ftMicroNormal-archetype</artifactId>
    <!-- 必须指定1.0，否则生成不了工程 -->
    <version>1.0</version>
    <packaging>maven-archetype</packaging>

    <!-- 设定Deploy仓库 -->
    <distributionManagement>
        <repository>
            <id>efuture-omni</id>
            <name>Internal Releases</name>
            <url>http://121.199.15.11:8081/nexus/content/repositories/efuture-omni</url>
        </repository>
    </distributionManagement>
=======================================================================================================================================
mvn clean package -DskipTests install
mvn deploy

=======================================================================================================================================
         groupId: 代表的是唯一的一个标识，比如一个组织或者一个项目的名字，基本上是唯一的，以这个maven为例就是org.apache.maven。
      artifactId: 代表的是名字，如果刚刚那个有点像身份证，这个就是类似姓名的东西。
　 　    version: 比较好理解，就是版本号，比如1.2.1版本这样的。
         package: 生成包
 interactiveMode: 交互模式，默认为true。
     　packaging: 是我们提供组件的类型，比如使用时打包成一个jar或者是一个war之类的，默认为打成jar包。
archetypeGroupId: 这个和刚刚那个很像，指的是原型唯一的标识archetypeArtifactId：原型的名字。
archetypeCatalog: 这个我例子里面没有，指的是查找规则。
=======================================================================================================================================

mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DgroupId=com.efuture -DartifactId=efuture-xxx -Dversion=0.0.1 -Dpackagin=jar -Dpackage=com.efuture -DinteractiveMode=false -DarchetypeGroupId=com.efuture -DarchetypeArtifactId=ftMicroNormal-archetype -DarchetypeCatalog=http://121.199.15.11:8081/nexus/content/repositories/efuture-omni/archetype-catalog.xml

Then, install it using the command: 
    mvn install:install-file -DgroupId=com.efuture -DartifactId=ftMicroNormal-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=/path/to/file

Alternatively, if you host your own repository you can deploy the file there: 
    mvn deploy:deploy-file -DgroupId=com.efuture -DartifactId=ftMicroNormal-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=/path/to/file -Durl=[url] -DrepositoryId=[id]


  com.efuture:ftMicroNormal-archetype:jar:1.0

from the specified remote repositories:
  efuture-omni (http://121.199.15.11:8081/nexus/content/groups/public, releases=true, snapshots=false),
  central (https://repo.maven.apache.org/maven2, releases=true, snapshots=false)

	at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:221)
	at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:154)
	at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:555)
	at org.apache.maven.archetype.downloader.DefaultDownloader.download(DefaultDownloader.java:70)
	... 25 more
Caused by: org.eclipse.aether.resolution.ArtifactResolutionException: Failure to find com.efuture:ftMicroNormal-archetype:jar:1.0 in http://121.199.15.11:8081/nexus/content/groups/public was cached in the local repository, resolution will not be reattempted until the update interval of efuture-omni has elapsed or updates are forced
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:444)
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolveArtifacts(DefaultArtifactResolver.java:246)
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolveArtifact(DefaultArtifactResolver.java:223)
	at org.eclipse.aether.internal.impl.DefaultRepositorySystem.resolveArtifact(DefaultRepositorySystem.java:294)
	at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:215)
	... 28 more
Caused by: org.eclipse.aether.transfer.ArtifactNotFoundException: Failure to find com.efuture:ftMicroNormal-archetype:jar:1.0 in http://121.199.15.11:8081/nexus/content/groups/public was cached in the local repository, resolution will not be reattempted until the update interval of efuture-omni has elapsed or updates are forced
	at org.eclipse.aether.internal.impl.DefaultUpdateCheckManager.newException(DefaultUpdateCheckManager.java:231)
	at org.eclipse.aether.internal.impl.DefaultUpdateCheckManager.checkArtifact(DefaultUpdateCheckManager.java:206)
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.gatherDownloads(DefaultArtifactResolver.java:585)
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.performDownloads(DefaultArtifactResolver.java:503)
	at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:421)
	... 32 more

创建模版工程	
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DgroupId=com.efuture -DartifactId=efuture-xxx -Dversion=0.0.1 -Dpackagin=jar -Dpackage=com.efuture -DinteractiveMode=false -DarchetypeGroupId=com.efuture -DarchetypeArtifactId=ftMicroNormal-archetype -DarchetypeCatalog=http://121.199.15.11:8081/nexus/content/repositories/efuture-omni/archetype-catalog.xml  
	