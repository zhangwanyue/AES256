## Introduction

* PBKDF2WithHmacSHA256 + AES256

对输入的`password`,使用`PBKDF2WithHmacSHA256`计算`256bit`的`hash`,这里会需要使用到一个`salt`,目的是为了增加随机性.

计算出的`256bit`的`hash`会作为`AES`加密算法的密钥,使用该密钥对内容进行加密.

**该方法不适用于Android,因为Android中目前不支持`PBKDF2WithHmacSHA256`**

* Blake2b + AES256

对输入的`password`,使用`Blake2b`计算`256bit`的`hash`.

计算出的`256bit`的`hash`会作为`AES`加密算法的密钥,使用该密钥对内容进行加密.

**该方法使用了一个第三方库`com.madgag.spongycastle:core:1.58.0.0`,可以用于Android**


## 关于`AES256`与`JavaSecurity policy`

Java中使用Java Cryptography extension的时候，Java运行时会强制限制某些密钥长度参数，当你使用AES128长度的密钥的时候，是没有限制的，而使用AES256则会受到密钥长度的限制。为解决这个问题，你需要去jdk官网下载`unlimited policy`，并替换原来的jdk中的policy文件（否则在编译时会报错：`InvalidKeyException Illegal key size`）。

* 下面以jdk8为例进行说明：

如果使用的是`jdk8-u151`以上的版本，则默认使用的是`unlimited policy`，不用再进行替换（**推荐直接装jdk8-u151以上的版本，就不会遇到这个问题了**）。而如果是`jdk8-u151`以下的版本，必须替换policy文件。

Jdk 8的`unlimited policy`文件下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html

将下载的文件解压，然后将解压出的文件中的`local_policy.jar`和`US_export_policy.jar`拷贝到`$JAVA_HOME/jre/lib/security`中（`$JAVA_HOME/jre/lib/security`中已经有这两个文件了，所以你需要替换它们）

* 关于该问题的参考链接：

https://golb.hplar.ch/2017/10/JCE-policy-changes-in-Java-SE-8u151-and-8u152.html

https://stackoverflow.com/questions/3862800/invalidkeyexception-illegal-key-size?noredirect=1&lq=1