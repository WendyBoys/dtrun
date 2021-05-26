import React, {useEffect, useState} from 'react';
import { findAll,deletes} from "./service";
import {Button, Modal, notification, Space, Spin, Table} from "antd";
import {connection} from "../datasource/service";


const Whitelist = (props) => {
    const [list, setList] = useState([]);
    const [WhiteListId, setWhiteListId] = useState([]);
    const [visible, setVisible] = useState(false);
    const [loading, setLoading] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [modalText, setModalText] = useState('');
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);


    const handleCancel = () => {
        setVisible(false);
    };


    useEffect(() => {
        fentch();
    }, []);


    const fentch = () => {
        setLoading(true)
        findAll().then((response) => {
            const result = response.data.message;
            const data = response.data.data;
            if (result === 'Success') {
                setList(data);
                setLoading(false)
            } else {
                setLoading(false)
            }
        });
    }



    const toCreate = () => {
        props.history.push('/sysmanage/create')
    }

    const testDts = (id) => {
        setLoading(true)
        connection({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
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

    const deleteWhiteList = (id) => {
        const selecteLength = selectedRowKeys.length;
        if (selecteLength <= 1) {
            setModalText('您确定要删除吗?');
            const idArray = [];
            idArray.push(id)
            setWhiteListId(idArray);
        } else {
            setModalText('您确定要删除选中的' + selecteLength + '个ip地址吗?');
            setWhiteListId(selectedRowKeys);
        }
        setVisible(true);

    }

    const handleOk = () => {
        setConfirmLoading(true);
        deletes({
            data: {
                id: WhiteListId,
            }
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setWhiteListId([]);
                setVisible(false);
                setConfirmLoading(false);
                fentch();
                notification['success']({
                    message: '通知',
                    description:
                        '删除成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '删除失败',
                    duration: 1,
                });
            }
        });
    };


    const updateDts = (id) => {
        props.history.push('/sysmanage/update/'+ id )
    }


    const columns = [
        {
            title: 'IP地址',
            dataIndex: 'ip',
            render: (text, record) => <span style={{color: '#0062FF', cursor: 'pointer'}}
                                            onClick={() => updateDts(record.id)}>{text}</span>,
        },

        {
            title: '创建时间',
            dataIndex: 'createTime',
        },
        {
            title: '操作',
            dataIndex: 'option',
            render: (text, record) => (
                <Space size="middle">
                    <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                          onClick={() => deleteWhiteList(record.id, record.contactName)}>删除</span>
                </Space>
            )
        },
    ];

    const onSelectChange = selectedRowKeys => {
        setSelectedRowKeys(selectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    return ((
        <Spin size="large" spinning={loading}>
            <div style={{height: '50px', paddingTop: '10px'}}>
                <Button type="primary" onClick={() => fentch()}
                        style={{
                            background: '#00b4ed',
                            position: 'absolute',
                            right: '83px',
                            zIndex: '999',
                            borderRadius: '5px'
                        }}
                >刷新</Button>
                <Button type="primary" onClick={() => toCreate()}
                        style={{
                            background: '#00b4ed',
                            position: 'absolute',
                            right: '11px',
                            zIndex: '999',
                            borderRadius: '5px'
                        }}
                >创建</Button></div>
            <div style={{height: '10px', background: '#f0f2f5'}}></div>
            <div style={{padding: '10px', background: '#ffffff'}}>
                <div>
                    <Table rowSelection={rowSelection} bordered
                           columns={columns}
                           dataSource={list}
                           pagination={{
                               showQuickJumper: true,
                               defaultPageSize: 10,
                               pageSizeOptions: [10, 20, 50, 100]
                           }}
                    />
                </div>
                <Modal
                    title="删除ip地址"
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
    ));

}

export default Whitelist;


