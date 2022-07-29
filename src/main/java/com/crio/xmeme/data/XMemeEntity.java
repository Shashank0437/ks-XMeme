package com.crio.xmeme.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "memes")
@NoArgsConstructor
public class XMemeEntity {

  @Id
  private String _id;
  @NotNull
  private String id;
  @NotNull
  private String name;
  @NotNull
  private String url;
  @NotNull
  private String caption;

}
