import { useState } from "react";
import DataTable from "react-data-table-component";

export default function CommonDataTable({
    columns,
    data,
    progressPending,
    pagination,
    paginationServer,
    paginationTotalRows,
    onChangeRowsPerPage,
    onChangePage,
    customStyles,
}) {
    const [loading, setLoading] = useState(false);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    return (
        <DataTable
            columns={columns}
            data={data}
            progressPending={loading}
            pagination
            paginationServer
            paginationTotalRows={paginationTotalRows}
            onChangeRowsPerPage={onChangeRowsPerPage}
            onChangePage={onChangePage}
            customStyles={customStyles}
        />
    );
}