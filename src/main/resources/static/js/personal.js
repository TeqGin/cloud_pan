$("#main").click(function () {
    location.href = "/user/personal"
})
$("#data_analyze").click(function () {
    location.href = "/user/personal"
})
$("#change_password").click(function () {
    location.href = "/user/change_password"
})
$("#change_name").click(function () {
    location.href = "/user/change_name"
})

$("#change_account").click(function () {
    location.href = "/user/change_account"
})


//获取hidden的内容
var graphx = document.getElementById('graphX').value
//转成json格式
var data_x = JSON.parse(graphx)
//创建数组
var data_formate = []
for(var key in data_x){
    data_formate.push({"value":data_x[key], "name": key})
}
var myecharts = echarts.init(document.getElementById('graph'))
option = {
    backgroundColor: '#FFFFFF',
    title: {
        text: '已上传文件统计',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#000000'
        }
    },

    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
    },

    series: [
        {
            name: '数据类型',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: data_formate.sort(function (a, b) { return a.value - b.value; }),
            roseType: 'radius',
            label: {
                color: 'rgb(0,0,0)'
            },
            labelLine: {
                lineStyle: {
                    color: 'rgb(0,0,0)'
                },
                smooth: 0.2,
                length: 10,
                length2: 20
            },
            itemStyle: {
                color: '#c23531',
                shadowBlur: 200,
                shadowColor: 'rgb(255,255,255)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
                return Math.random() * 200;
            }
        }
    ]
};
myecharts.setOption(option)