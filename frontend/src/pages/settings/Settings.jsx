import { useState } from 'react';

import AccountSettings from './Account/AccountSettings';
import NotificationSettings from './NotificationSettings';
import UseGuide from './UseGuide';

export default function Settings() {
  const [settingId, setSettingId] = useState(0);

  return (
    <div className="settings-container">
      <div className="settings-menu-container">
        <div
          className={`settings-menu ${settingId === 0 ? 'selected' : ''}`}
          onClick={() => {
            setSettingId(0);
          }}
        >
          <p className="settings-menu-text">계정</p>
        </div>
        <div
          className={`settings-menu ${settingId === 1 ? 'selected' : ''}`}
          onClick={() => {
            setSettingId(1);
          }}
        >
          <p className="settings-menu-text">알림</p>
        </div>
        <div
          className={`settings-menu ${settingId === 2 ? 'selected' : ''}`}
          onClick={() => {
            setSettingId(2);
          }}
        >
          <p className="settings-menu-text">이용약관</p>
        </div>
      </div>
      {settingId === 0 && <AccountSettings />}
      {settingId === 1 && <NotificationSettings />}
      {settingId === 2 && <UseGuide />}
    </div>
  );
}
