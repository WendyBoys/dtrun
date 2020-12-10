import React, { useState, useEffect } from 'react';
import Nav from '../common/Nav'
import axios from 'axios';
import { notification, Modal, Spin } from 'antd';


const Show = (props) => {
    const [list, setList] = useState([]);
    const [dtsId, setDtsId] = useState(-1);
    const [visible, setVisible] = useState(false);
    const [loading, setLoading] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [modalText, setModalText] = useState('');


    const handleOk = () => {
        setConfirmLoading(true);
        axios.delete('/dtsource/delete', {
            data: {
                id: dtsId,
            }
        }).then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                setVisible(false);
                setConfirmLoading(false);
                fentch();
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '删除失败！',
                    duration: 1,
                });


            }


        });


    };

    const handleCancel = () => {
        setVisible(false);
    };


    useEffect(() => {
        fentch();
    }, []);


    const fentch = () => {
        setLoading(true)
        axios.get('/dtsource/findAll').then((response) => {
            var result = response.data.message;
            var data = response.data.data;
            if (result == 'Success') {
                setList(data);
                setLoading(false)
            } else {

            }
        });
    }


    const toCreate = () => {
        props.history.push('/datasource/create')
    }

    const testDts = (id) => {
        setLoading(true)
        axios.post('/dtsource/connection', {
            id: id,
        }).then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                setLoading(false)
                notification['success']({
                    message: '通知',
                    description:
                        '测试成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '测试失败，请检查您的参数设置',
                    duration: 1,
                });
            }
            setLoading(false)
        });
    }


    const deleteDts = (id, dtSourceName) => {
        setDtsId(id);
        setModalText('您确定要删除数据源 ' + dtSourceName + ' 吗?');
        setVisible(true);
    }


    const updateDts = (id) => {
        props.history.push('/datasource/update/' + id)
    }


    const container = ((

        <div className="content-wrapper">
            <Nav />

            <div className="content-body">

                <Spin size="large" spinning={loading}>

                    <div id="listdts" className="content">
                        <div className="row" style={{ marginBottom: '10px' }}>
                            <div className="col-xl-12 ">
                                <button className="btn btn-primary pull-right" onClick={() => toCreate()}>创建</button>
                            </div>
                        </div>


                        <div className="row">

                            <div className="col-xl-12 components-sidebar">
                                <table id="table" className="table">
                                    <thead>
                                        <tr>
                                            <th scope="col">序号</th>
                                            <th scope="col">数据源名称</th>
                                            <th scope="col">数据源类型</th>
                                            <th scope="col">创建时间</th>
                                            <th scope="col">操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="dtsource">
                                        {
                                            list.map((item, index) =>
                                                <tr key={index}>
                                                    <td>{index + 1}</td>
                                                    <td><span
                                                        style={{ color: '#0062FF', cursor: 'pointer', marginRight: '10px' }}
                                                        onClick={() => updateDts(item.id)}>{item.dtSourceName}</span></td>
                                                    <td>{item.dtSourceType}</td>
                                                    <td>{item.createTime}</td>
                                                    <td><span
                                                        style={{ color: '#0062FF', cursor: 'pointer', marginRight: '10px' }}
                                                        onClick={() => testDts(item.id)}>测试</span> <span
                                                            style={{ color: '#0062FF', cursor: 'pointer' }}
                                                            onClick={() => deleteDts(item.id, item.dtSourceName)}>删除</span></td>
                                                </tr>
                                            )
                                        }
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <Modal
                            title="删除数据源"
                            okText="确定"
                            cancelText="取消"
                            visible={visible}
                            onOk={handleOk}
                            confirmLoading={confirmLoading}
                            onCancel={handleCancel}
                        >
                            <p>{modalText}</p>
                        </Modal>
                    </div>
                </Spin>
            </div>
        </div>

    ));

    return container
}


export default Show;
