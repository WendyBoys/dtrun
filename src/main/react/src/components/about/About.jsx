import React, {useEffect, useState} from 'react';
import {Col, Image, Row, Spin} from 'antd';

const About = () => {
    return <div style={{textAlign: 'center', height: '100%', padding: '10px'}}>
        <Row style={{height: '52%'}}>
            <Col span={24} style={{height: '100%'}}>
                <img src='https://gw.alipayobjects.com/mdn/rms_ae7ad9/afts/img/A*-wAhRYnWQscAAAAAAAAAAABkARQnAQ'
                     height='100%'/>
            </Col>
        </Row>
        <Row style={{height: '5%', margin: '5px 0'}}>
            <Col span={24}>
                <div>DTRUN迁移系统是一个无侵入式的对象存储迁移系统,它可以将不同平台的对象存储数据进行平行迁移</div>
                <span>在数据进行迁移的时候不会影响您业务的正常使用并且您无需关注过程,当迁移任务完成后,您将会收到通知</span>
            </Col>
        </Row>
        <Row style={{height: '40%'}}>
            <Col span={6}>
                <Image
                    src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/1.png'
                    placeholder={
                        <Image
                            preview={false}
                            src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/1.png'
                        />
                    }
                />
                <div>支持多种数据源迁移</div>
            </Col>
            <Col span={6}>
                <Image
                    src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/2.png'
                    placeholder={
                        <Image
                            preview={false}
                            src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/2.png'
                        />
                    }
                />
                <div>简单配置，开箱即用</div>
            </Col>
            <Col span={6}> <Image
                src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/4.png'
                placeholder={
                    <Image
                        preview={false}
                        src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/4.png'
                    />
                }
            />
                <div>自动化迁移，无需人工二次干预</div>
            </Col>
            <Col span={6}> <Image
                src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/5.png'
                placeholder={
                    <Image
                        preview={false}
                        src='https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/5.png'
                    />
                }
            />
                <div>无侵入式设计,迁移时不影响业务使用</div>
            </Col>

        </Row>

        <Row style={{height: '3%'}}>
            <Col span={24}>
                <span>© 20017-2021 Dtrun.cn 版权所有</span>
            </Col>
        </Row>

    </div>

}

export default About;