import React, { FC, useEffect, useState } from 'react';
import { Tabs } from 'antd';
import { Product } from '../../types/MockTypes/Product';
import PostGrid from '../PostGrid/PostGrid';

interface NewTabsProps {
  label: string[]
}

const NewTabs: FC<NewTabsProps> = ({ label }) => {
    const [activeTabKey, setActiveTabKey] = useState<string>('1');

    useEffect(() => {
        // Функция, которая будет вызываться при изменении активной вкладки
        console.log('Активная вкладка изменена:', activeTabKey);
    }, [activeTabKey]);

    return (
        <Tabs
            defaultActiveKey="1"
            type="card"
            size="large"
            onChange={(key) => setActiveTabKey(key)}
        >
            {label.map((_, index) => (
                <Tabs.TabPane
                    key={(index + 1).toString()}
                    tab={label[index]}
                >
                    <PostGrid/>
                </Tabs.TabPane>
            ))}
        </Tabs>
    );
};

export default NewTabs;