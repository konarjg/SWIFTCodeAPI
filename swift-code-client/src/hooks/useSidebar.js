import {React, useRef} from 'react';

export function useSidebar() {
    const sidebarRef = useRef(null);

    const handleSidebar = () => {
        sidebarRef.current.style.left = sidebarRef.current.style.left === "0vw" ? "-21vw" : "0vw";
    }

    return [sidebarRef, handleSidebar];
}