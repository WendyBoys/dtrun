import React, {useEffect, useState} from 'react';
import {Line, RingProgress} from '@ant-design/charts';
import {Col, Row, Alert, Result, Button, Carousel, Image} from 'antd';
import {NavLink} from "react-router-dom";
import cookie from "react-cookies";
import {login} from "../user/service";
import {notification} from "antd/lib/index";
import {getMessage} from "../user/service";


const contentStyle = {
    height: '510px',
    color: '#fff',
    width: '100%',
    textAlign: 'center',
    background: '#364d79',
};

const Dashboard = () => {

    const [data, setData] = useState([]);

    useEffect(() => {
        fentch();
    }, []);

    const fentch = () => {
        getMessage().then((response) => {
            const result = response.data.message;
            const data2 = response.data.data;
            if (result === 'Success') {
                setData(JSON.parse(data2));
            }
        });
    }


    const config = {
        data: data,
        xField: 'date',
        yField: 'value',
        yAxis: {
            label: {
                formatter: function formatter(v) {
                    return ''.concat(v).replace(/\d{1,3}(?=(\d{3})+$)/g, function (s) {
                        return ''.concat(s, ',');
                    });
                },
            },
        },
        legend: {position: 'top'},
        seriesField: 'type',
        color: function color(_ref) {
            const type = _ref.type;
            return type === '运行内存' ? '#F4664A' : type === '磁盘用量' ? '#30BF78' : '#FAAD14';
        },
        lineStyle: function lineStyle(_ref2) {
            const type = _ref2.type;
            if (type === 'register') {
                return {
                    lineDash: [4, 4],
                    opacity: 1,
                };
            }
            return {opacity: 0.5};
        },
    };

    return <div style={{height: '40%'}}>
        <Row style={{height: '100%', padding: '10px', textAlign: 'center'}}>
            <Col span={8} style={{height: '40%', paddingRight: '10px'}}>
                <Result
                    status="success"
                    title="欢迎使用DTRUN迁移系统"
                    subTitle="Order number: 2017182818828182881 Cloud server configuration takes 1-5 minutes, please wait."
                    extra={[
                        <Button type="primary" key="console">
                            <NavLink to="/datasource/show">创建数据源</NavLink>
                        </Button>,
                        <Button key="buy">使用教程</Button>,
                    ]}
                />
            </Col>
            <Col span={16}><Line {...config}
                                 style={{maxHeight: '500px', height: '40%', minHeight: '350px'}}/></Col>


        </Row>
        <Carousel autoplay style={{padding: '20px 20px 0 20px'}}>
            <div>
                <Image
                    src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/a.jpg'
                    style={contentStyle}
                    width='100%'
                    placeholder={
                        <Image
                            preview={false}
                            src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/a.jpg'
                        />
                    }
                />
            </div>
            <div>
                <Image
                    src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/b.jpg'
                    style={contentStyle}
                    width='100%'
                    placeholder={
                        <Image
                            preview={false}
                            src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/b.jpg'
                        />
                    }
                />
            </div>
            <div>
                <Image
                    src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/c.jpg'
                    style={contentStyle}
                    width='100%'
                    placeholder={
                        <Image
                            preview={false}
                            src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/c.jpg'
                        />
                    }
                />
            </div>
        </Carousel>


    </div>


}

export default Dashboard;