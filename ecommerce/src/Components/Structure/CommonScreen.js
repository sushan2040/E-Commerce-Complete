import { useState } from "react";
import SidebarComponent from "./SidebarComponent";
import { Outlet } from "react-router-dom";
import useCommonEffect from "../Session/useCommonEffect";

export default function CommonScreen() {
  const [collapsed, setCollapsed] = useState(true);
  const [isFullscreen, setIsFullscreen] = useState(false);

  const toggleSidebar = () => setCollapsed(!collapsed);

  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
      setIsFullscreen(true);
    } else if (document.exitFullscreen) {
      document.exitFullscreen();
      setIsFullscreen(false);
    }
  };
  return (
    <>
      <div className="layout">
        <SidebarComponent collapsed={collapsed} />

        <div className="content">
          <Outlet />
        </div>

      </div>
    </>
  )

}