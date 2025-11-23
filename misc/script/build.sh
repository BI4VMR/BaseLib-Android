#!/bin/bash

CONFIG_LIBS="misc/script/config_libs"


# 判断是否在工程根目录
if [[ ! -f "settings.gradle.kts" ]]; then
    echo "请在工程根目录下运行此脚本！"
    exit 1
fi


# 清空组件在本地的缓存
echo "----- 清除本地Gradle缓存... -----"

find ~/.gradle/caches -iname "*net.bi4vmr.tool.java*" -print0 | xargs -t -r -0 -n 1 rm -rf

./gradlew clean --no-daemon

echo "----- 清除本地Gradle缓存完成。 -----"


echo -e "\n----- 发布Lib组件... -----"

while IFS=$'\n' read -r module; do
    # 跳过空行和注释行
    [[ -z "$module" || "$module" =~ ^# ]] && continue

    echo -e "\n----- 编译模块：[$module] ... -----"

    # Gradle命令默认会读取标准输入，和循环语句的 `read` 命令冲突导致逻辑错误，此处需要重定向标准输入。
    ./gradlew "$module:publish" --no-daemon < /dev/null

    if [[ $? -ne 0 ]]; then
        echo "模块 [$module] 发布失败，编译终止！"
        exit 1
    fi

    echo "----- 编译模块：[$module] 完成。 -----"
done < "$CONFIG_LIBS"

echo -e "\n----- 发布Lib组件完成。 -----"
