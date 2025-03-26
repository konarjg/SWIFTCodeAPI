import {React, useRef, useState} from 'react';
import {TopNav} from '../components/TopNav';
import { Sidebar } from '../components/Sidebar';
import { useSidebar } from '../hooks/useSidebar';
import styles from '../styles/GetSwiftCodeDetails.module.css';
import { HeadquarterCodeDetails } from '../components/HeadquarterCodeDetails';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from "@fortawesome/free-solid-svg-icons"
import { useSwiftCodeService } from '../api/useSwiftCodeService';

export function GetSwiftCodeDetails() {
    const [sidebarRef, handleSidebar] = useSidebar();
    const searchFieldRef = useRef(null);
    const [getSwiftCodeDetails] = useSwiftCodeService();
    const [details, setDetails] = useState(null);

    function handleSearchChange() {
        const input = searchFieldRef.current.value;

        searchFieldRef.current.value = input.toUpperCase();
    }

    const handleSearch = async (event) => {
        event.preventDefault();
        const swiftCode = searchFieldRef.current.value;
        const details = await getSwiftCodeDetails(swiftCode);
        setDetails(details);
    }

    return (
        <main className={styles.page}>
            <Sidebar ref={sidebarRef}/>

            <header className={styles.header}>
                <TopNav handleSidebar={handleSidebar}/>
            </header>

            <section className={styles.content}>
                <form className={styles.searchForm}>
                    <label className={styles.searchLabel}>Find SWIFT code details</label>
                    <input ref={searchFieldRef} type="text" className={styles.searchField} minLength="11" maxLength="11" onChange={handleSearchChange}/>
                    <button className={styles.searchButton} onClick={(event) => handleSearch(event)}><FontAwesomeIcon icon={faSearch} /></button>
                </form>

                {
                    (details !== null && details.isHeadquarter) && <HeadquarterCodeDetails code={details}/>
                }
            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}