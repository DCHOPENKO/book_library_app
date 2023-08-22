package com.book.library.integration;

import com.book.library.integration.annotation.IT;
import org.springframework.test.context.jdbc.Sql;

@IT
@Sql({
        "classpath:sql/data.sql"
})
public abstract class IntegrationBaseTest {
}
