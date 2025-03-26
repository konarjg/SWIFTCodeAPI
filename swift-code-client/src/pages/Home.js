import {React} from 'react';
import {TopNav} from '../components/TopNav';
import styles from '../styles/Home.module.css';
import { Sidebar } from '../components/Sidebar';
import { useSidebar } from '../hooks/useSidebar';

export function Home() {
    const [sidebarRef, handleSidebar] = useSidebar();

    return (
        <main className={styles.page}>
            <Sidebar ref={sidebarRef}/>

            <header className={styles.header}>
                <TopNav handleSidebar={handleSidebar}/>
            </header>

            <section className={styles.content}>
                <p>Welcome to the Swift Code Database web page, created as part of the Remitly 2025 internship assignment! This platform is designed to make managing SWIFT codes straightforward and accessible, enabling users to retrieve and organize financial information seamlessly.</p>

                <p>Here’s what you can do on this page:</p>
                <ul>
                    <li><strong>Retrieve SWIFT Codes:</strong> Access stored SWIFT codes to identify financial institutions across the globe.</li>
                    <li><strong>Search by Country:</strong> Locate SWIFT codes specific to a country using its ISO 2 code.</li>
                    <li><strong>Add New Codes:</strong> Easily populate the database with additional SWIFT codes whenever required.</li>
                    <li><strong>Remove Unnecessary Codes:</strong> Delete outdated or incorrect SWIFT codes to keep the database organized.</li>
                </ul>

                <p>All of these features can be accessed via the sidebar, designed for quick and simple navigation.</p>

                <p>To use the sidebar, click the <strong>hamburger menu</strong> in the top left corner of the page. The sidebar will slide into view, offering clear and intuitive options for managing SWIFT codes:</p>
                <ul>
                    <li>Explore options for adding, retrieving, searching, and removing codes.</li>
                    <li>Easily navigate through different sections of the page.</li>
                    <li>Enjoy an intuitive interface focused on simplicity and efficiency.</li>
                </ul>

                <p>This webpage blends functionality with an elegant design, making the process of organizing and exploring SWIFT codes effortless and satisfying. Click the menu and let the sidebar guide you to an efficient way of handling SWIFT codes!</p>

            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}