package com.crio.xmeme.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class MemeDto {
  private String id;

  private String name;

  private String url;

  private String caption;
}
